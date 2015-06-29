package parser;//------------------------------------------------------------------//
//COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
//Copyright (c) 2008, Francisco Jos� Moreno Velo                   //
//All rights reserved.                                             //
////
//Redistribution and use in source and binary forms, with or       //
//without modification, are permitted provided that the following  //
//conditions are met:                                              //
////
//* Redistributions of source code must retain the above copyright //
//notice, this list of conditions and the following disclaimer.  // 
////
//* Redistributions in binary form must reproduce the above        // 
//copyright notice, this list of conditions and the following    // 
//disclaimer in the documentation and/or other materials         // 
//provided with the distribution.                                //
////
//* Neither the name of the University of Huelva nor the names of  //
//its contributors may be used to endorse or promote products    //
//derived from this software without specific prior written      // 
//permission.                                                    //
////
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
//CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
//DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
//BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
//EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
//TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
//DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
//ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
//IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
//THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//Universidad de Huelva                       //
//Departamento de Tecnolog�as de la Informaci�n          //
//�rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
////
//Compilador del lenguaje Tinto [Versi�n 0.0]             //
////
//------------------------------------------------------------------//


import ast.Type;
import ast.expression.*;
import ast.statements.*;
import ast.structs.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class TORCSParser implements TokenConstants {


	private TORCSLexer lexer;

    private Token nextToken;

    private SymbolTable symbolTable;

	public boolean parse(String filename) throws Exception{
		try {
			this.lexer = new TORCSLexer(filename);
            this.nextToken = lexer.getNextToken();
            this.symbolTable = new SymbolTable(this);
            Controller ASA = parseController();
            System.out.println(ASA.genJavaCode(filename));
            return nextToken.getKind() == EOF;
		} catch (Exception ex) {
			System.out.println(ex.toString());
		    return false;
		}
	}


	public static void main(String[] args) throws Exception{
		if(args.length == 0) return;

		TORCSParser parser = new TORCSParser();
		if(parser.parse(args[0])) {
			System.out.println("Correcto");
		} else {
			System.out.println("Incorrecto");
		}
	}


	private String match(int kind) throws SintaxException {
		if(nextToken.getKind() == kind) {
            String lex = nextToken.getLexeme();
            nextToken = lexer.getNextToken();
            return lex;
        } else throw new SintaxException(nextToken,kind);
	}


    private Controller parseController() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INNER:
            case PERCEPTION:
            case ACTION:
            case RULES:
                List<Declaration> declarations = parseListDeclaration();
                //TODO Check names declarations
                Rules rules = parseRules();
                return new Controller(declarations, rules);
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION, RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Declaration> parseListDeclaration() throws SintaxException, SemanticException{
        switch (nextToken.getKind()) {
            case INNER:
            case PERCEPTION:
            case ACTION:
                Declaration firstDeclaration = parseDeclaration();
                List<Declaration> declarations = new LinkedList<Declaration>();
                declarations.add(firstDeclaration);

                List<Declaration> restOfDeclarations = parseListDeclaration();
                declarations.addAll(restOfDeclarations);
                return declarations;
            case RULES:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION, RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Declaration parseDeclaration() throws SintaxException, SemanticException{
        switch (nextToken.getKind()) {
            case INNER:
                InnerDecl inner = parseInnerDecl();
                symbolTable.addDeclaration(inner.getVar().name, inner);
                return inner;
            case PERCEPTION:
                Perception perc = parsePerceptionDecl();
                symbolTable.addDeclaration(perc.getTypedName(), perc);
                return perc;
            case ACTION:
                Action act = parseActionDecl();
                symbolTable.addDeclaration(act.getTypedName(), act);
                return act;
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private InnerDecl parseInnerDecl() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INNER:
                match(INNER);
                int type = parseType();
                String name = match(IDENTIFIER);
                match(ASSIGN);
                Literal lit = parseLiteral();
                match(SEMICOLON);
                Variable innerVar = new Variable(name, type, !readOnly);
                symbolTable.addVariable(innerVar);
                if(Variable.checkMatchingType(type, lit.getType()))
                    return new InnerDecl(innerVar , lit);
                else
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
            default: {
                int[] expected = {INNER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private int parseType() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
                match(INT);
                return Literal.INT_TYPE;
            case BOOLEAN:
                match(BOOLEAN);
                return Literal.BOOLEAN_TYPE;
            case DOUBLE:
                match(DOUBLE);
                return Literal.DOUBLE_TYPE;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Perception parsePerceptionDecl() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case PERCEPTION:
                match(PERCEPTION);
                String name = match(IDENTIFIER);

                symbolTable.createContext();

                Perception per = new Perception(name, symbolTable);
                List<Variable> arguments = parseArgumentDecl();

                for (Variable argument: arguments) {
                    per.addArgument(argument);
                }

                PcptBlockStm body = parsePerceptionBody();
                per.setBody(body);

                symbolTable.deleteContext();

                if (per.checkReturn())
                    return per;
                else throw new SemanticException(SemanticException.PERCEPTION_DOESNT_RETURN, nextToken, name);
            default: {
                int[] expected = {PERCEPTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Action parseActionDecl() throws SintaxException, SemanticException{
        switch (nextToken.getKind()) {
            case ACTION:
                match(ACTION);
                String name = match(IDENTIFIER);

                symbolTable.createContext();

                Action action = new Action(name, symbolTable);

                List<Variable> arguments = parseArgumentDecl();

                for(Variable argument: arguments) {
                    action.addArgument(argument);
                }

                ActBlockStm body = parseActionBody();
                action.setBody(body);

                symbolTable.deleteContext();
                return action;
            default: {
                int[] expected = {ACTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Variable> parseArgumentDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case LPAREN:
                match(LPAREN);
                List<Variable> arguments = parseListArguments();
                match(RPAREN);
                return arguments;
            default: {
                int[] expected = {LPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Variable> parseListArguments() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                Variable firstArgument = parseArgument();
                List<Variable> otherArguments = MoreArguments();

                List<Variable> arguments = new LinkedList<Variable>();
                arguments.add(firstArgument);
                arguments.addAll(otherArguments);
                return arguments;
            case RPAREN:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Variable> MoreArguments() throws SintaxException{
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                Variable firstArgument = parseArgument();
                List<Variable> otherArguments = MoreArguments();

                List<Variable> arguments = new LinkedList<Variable>();
                arguments.add(firstArgument);
                arguments.addAll(otherArguments);
                return arguments;
            case RPAREN:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {COMMA, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Variable parseArgument() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                int type = parseType();
                String name = match(IDENTIFIER);

                return new Variable(name, type, readOnly);
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptBlockStm parsePerceptionBody() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                List<PcptStatement> statements = ListPcptStatement();
                match(RBRACE);
                return new PcptBlockStm(statements); //TODO check return
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<PcptStatement> ListPcptStatement() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
            case IDENTIFIER:
            case IF:
            case WHILE:
            case TRUE:
            case FALSE:
            case LBRACE:
                PcptStatement firstStatement = parsePcptStatement();
                List<PcptStatement> others = ListPcptStatement();

                List<PcptStatement> statements = new LinkedList<PcptStatement>();
                statements.add(firstStatement);
                statements.addAll(others);

                return statements;
            case RBRACE:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE, LBRACE, RBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActBlockStm parseActionBody() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                List<ActStatement> statements = parseListActStatement();
                match(RBRACE);

                return new ActBlockStm(statements);
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<ActStatement> parseListActStatement() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
            case IDENTIFIER:
            case IF:
            case WHILE:
            case LBRACE:
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                ActStatement firstStatement = parseActStatement();
                List<ActStatement> others = parseListActStatement();

                List<ActStatement> statements = new LinkedList<ActStatement>();
                statements.add(firstStatement);
                statements.addAll(others);
                return statements;
            case RBRACE:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, LBRACE, GEAR,
                        ACCELERATE, BRAKE, STEERING, RBRACE };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Rules parseRules() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case RULES:
                match(RULES);
                match(LBRACE);
                List<Rule> rules = parseListRules();
                match(RBRACE);

                return new Rules(rules);
            default: {
                int[] expected = {RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Rule> parseListRules() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Rule firsRule = parseRule();
                List<Rule> others = parseListRules();

                List<Rule> rules = new LinkedList<Rule>();
                rules.add(firsRule);
                rules.addAll(others);

                return rules;
            case RBRACE:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18, RBRACE
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private InputVar parseInputVar() throws SintaxException{
        String name;
        switch (nextToken.getKind()) {
            case GEAR:
                name = match(GEAR);
                return new InputVar(name);
            case SPEED:
                name = match(SPEED);
                return new InputVar(name);
            case ANGLE:
                name = match(ANGLE);
                return new InputVar(name);
            case POSITION:
                name = match(POSITION);
                return new InputVar(name);
            case RPM:
                name = match(RPM);
                return new InputVar(name);
            case SENSOR_0:
                name = match(SENSOR_0);
                return new InputVar(name);
            case SENSOR_1:
                name = match(SENSOR_1);
                return new InputVar(name);
            case SENSOR_2:
                name = match(SENSOR_2);
                return new InputVar(name);
            case SENSOR_3:
                name = match(SENSOR_3);
                return new InputVar(name);
            case SENSOR_4:
                name = match(SENSOR_4);
                return new InputVar(name);
            case SENSOR_5:
                name = match(SENSOR_5);
                return new InputVar(name);
            case SENSOR_6:
                name = match(SENSOR_6);
                return new InputVar(name);
            case SENSOR_7:
                name = match(SENSOR_7);
                return new InputVar(name);
            case SENSOR_8:
                name = match(SENSOR_8);
                return new InputVar(name);
            case SENSOR_9:
                name = match(SENSOR_9);
                return new InputVar(name);
            case SENSOR_10:
                name = match(SENSOR_10);
                return new InputVar(name);
            case SENSOR_11:
                name = match(SENSOR_11);
                return new InputVar(name);
            case SENSOR_12:
                name = match(SENSOR_12);
                return new InputVar(name);
            case SENSOR_13:
                name = match(SENSOR_13);
                return new InputVar(name);
            case SENSOR_14:
                name = match(SENSOR_14);
                return new InputVar(name);
            case SENSOR_15:
                name = match(SENSOR_15);
                return new InputVar(name);
            case SENSOR_16:
                name = match(SENSOR_16);
                return new InputVar(name);
            case SENSOR_17:
                name = match(SENSOR_17);
                return new InputVar(name);
            case SENSOR_18:
                name = match(SENSOR_18);
                return new InputVar(name);
            default: {
                int[] expected = {GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2,
                        SENSOR_3, SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9,
                        SENSOR_10, SENSOR_11, SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16,
                        SENSOR_17, SENSOR_18};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private OutputVar parseOutputVar() throws SintaxException{
        String name;
        switch (nextToken.getKind()) {
            case GEAR:
                name = match(GEAR);
                return new OutputVar(name);
            case ACCELERATE:
                name = match(ACCELERATE);
                return new OutputVar(name);
            case BRAKE:
                name = match(BRAKE);
                return new OutputVar(name);
            case STEERING:
                name = match(STEERING);
                return new OutputVar(name);
            default: {
                int[] expected = {GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptStatement parsePcptStatement() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                return parsePcptDecl();
            case IDENTIFIER:
                return parsePcptAssignStm();
            case IF:
                return parsePcptIfStm();
            case WHILE:
                return parsePcptWhileStm();
            case TRUE:
                return parsePcptTrueStm();
            case FALSE:
                return parsePcptFalseStm();
            case LBRACE:
                return parsePcptBlockStm();
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE, LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptDecl parsePcptDecl() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                int type = parseType();
                String name = match(IDENTIFIER);
                Variable localVar = new Variable(name, type, !readOnly); //TODO ControlLocales
                symbolTable.addVariable(localVar);

                Expression assign = parseOptAssign(type);
                match(SEMICOLON);

                if (assign != null)
                    return new PcptDecl(localVar, assign);
                else return new PcptDecl(localVar);
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseOptAssign(int type) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case ASSIGN:
                match(ASSIGN);
                Expression expression = parseExpr();

                if(Variable.checkMatchingType(type, expression.getType()))
                    return expression;
                else throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
            case SEMICOLON:
                return null;
            default: {
                int[] expected = {ASSIGN, SEMICOLON};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptIfStm parsePcptIfStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IF:
                match(IF);
                match(LPAREN);
                Expression condition = parseExpr();
                if(condition.getType() != Expression.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                match(RPAREN);
                PcptStatement thenCode = parsePcptStatement();
                PcptStatement elseCode = PcptElse();
                return new PcptIfStm(condition,thenCode, elseCode); //TODO CHEK CORRECT RETURN
            default: {
                int[] expected = {IF};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptStatement PcptElse() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case ELSE:
                match(ELSE);
                return parsePcptStatement(); // TODO CHEK CORRECT RETURN
            case INT:
            case BOOLEAN:
            case DOUBLE:
            case IDENTIFIER:
            case IF:
            case WHILE:
            case TRUE:
            case FALSE:
            case LBRACE:
            case RBRACE:
                return null;
            default: {
                int[] expected = {ELSE, INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE,
                        LBRACE, RBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptWhileStm parsePcptWhileStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case WHILE:
                match(WHILE);
                match(LPAREN);
                Expression condition = parseExpr();
                if (condition.getType() != Expression.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                match(RPAREN);

                PcptStatement whileCode = parsePcptStatement();
                return new PcptWhileStm(condition, whileCode);
            default: {
                int[] expected = {WHILE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptAssignStm parsePcptAssignStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                String name = match(IDENTIFIER);
                Variable var = symbolTable.getVariable(name);
                if (var == null)
                    throw new SemanticException(SemanticException.VAR_NOT_DECLARED, nextToken);
                else if (var.isReadOnly())
                    throw new SemanticException(SemanticException.ONLY_READ_VAR, nextToken, name);
                match(ASSIGN);
                Expression expression = parseExpr();
                match(SEMICOLON);
                if (Variable.checkMatchingType(var.getType(), expression.getType()))
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                else return new PcptAssignStm(var, expression);
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptReturnStm parsePcptTrueStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case TRUE:
                match(TRUE);
                match(SEMICOLON);
                return new PcptReturnStm(new BooleanLiteralExpression(true));
            default: {
                int[] expected = {TRUE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptReturnStm parsePcptFalseStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case FALSE:
                match(FALSE);
                match(SEMICOLON);
                return new PcptReturnStm(new BooleanLiteralExpression(false));
            default: {
                int[] expected = {FALSE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private PcptBlockStm parsePcptBlockStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                symbolTable.createContext();
                List<PcptStatement> statements = ListPcptStatement();
                symbolTable.deleteContext();
                match(RBRACE);
                return new PcptBlockStm(statements);
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActStatement parseActStatement() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                return parseActDecl();
            case IDENTIFIER:
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                return parseActAssignStm();
            case IF:
                return parseActIfStm();
            case WHILE:
                return parseActWhileStm();
            case LBRACE:
                return parseActBlockStm();
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, GEAR, ACCELERATE, BRAKE, STEERING,
                        IF, WHILE, LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActDecl parseActDecl() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                int type = parseType();
                String name = match(IDENTIFIER);

                Variable localVar = new Variable(name, type, !readOnly); //TODO CHECK VARIABLE
                symbolTable.addVariable(localVar);

                Expression assign = parseOptAssign(type);
                match(SEMICOLON);

                if (assign == null)
                    return new ActDecl(localVar);
                else return new ActDecl(localVar, assign);
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActIfStm parseActIfStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IF:
                match(IF);
                match(LPAREN);
                Expression condition = parseExpr();
                if (condition.getType() != Expression.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                match(RPAREN);

                ActStatement thenCode = parseActStatement();
                ActStatement elseCode = parseActElse();

                return new ActIfStm(condition, thenCode, elseCode); //TODO MAYBE CHECK??
            default: {
                int[] expected = {IF};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActStatement parseActElse() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case ELSE:
                match(ELSE);
                return parseActStatement();
            case INT:
            case BOOLEAN:
            case DOUBLE:
            case IDENTIFIER:
            case IF:
            case WHILE:
            case LBRACE:
            case RBRACE:
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                return null;
            default: {
                int[] expected = {ELSE, INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, LBRACE, RBRACE,
                        GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActWhileStm parseActWhileStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case WHILE:
                match(WHILE);
                match(LPAREN);
                Expression condition = parseExpr();
                if (condition.getType() != Expression.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken); //TODO types
                match(RPAREN);
                ActStatement whileCode = parseActStatement();

                return new ActWhileStm(condition, whileCode);
            default: {
                int[] expected = {WHILE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActAssignStm parseActAssignStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                String name = match(IDENTIFIER);
                Variable var = symbolTable.getVariable(name);
                if (var == null)
                    throw new SemanticException(SemanticException.VAR_NOT_DECLARED, nextToken);
                else if (var.isReadOnly())
                    throw new SemanticException(SemanticException.ONLY_READ_VAR, nextToken, name);
                match(ASSIGN);
                Expression assign = parseExpr();
                match(SEMICOLON);
                if (Variable.checkMatchingType(var.getType(), assign.getType()))
                    return new ActAssignStm(var, assign);
                else throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                OutputVar outVar = parseOutputVar();
                match(ASSIGN);
                Expression outExpression = parseExpr();
                match(SEMICOLON);
                if (Variable.checkMatchingType(outVar.getType(), outExpression.getType()))
                    return new ActAssignStm(outVar, outExpression);
                else throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
            default: {
                int[] expected = {IDENTIFIER, GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActBlockStm parseActBlockStm() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                symbolTable.createContext();
                List<ActStatement> statements = parseListActStatement();
                symbolTable.createContext();
                match(RBRACE);
                return new ActBlockStm(statements);
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Rule parseRule() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            //PRI(EXPR)
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                //END PRI(EXPR)
                Expression condition = parseExpr();
                if(condition.getType() != Expression.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                match(ARROW);
                ActionCallExpression firstActionCall = parseActionCall();
                List<ActionCallExpression> othersActionCall = parseMoreActionCall();
                match(SEMICOLON);

                List<ActionCallExpression> actionCalls = new LinkedList<ActionCallExpression>();
                actionCalls.add(firstActionCall);
                actionCalls.addAll(othersActionCall);
                return new Rule(condition, actionCalls);

            default: {
                int[] expected = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<ActionCallExpression> parseMoreActionCall() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                ActionCallExpression firstActionCall = parseActionCall();
                List<ActionCallExpression> othersActionCall = parseMoreActionCall();

                List<ActionCallExpression> actionCalls = new LinkedList<ActionCallExpression>();
                actionCalls.add(firstActionCall);
                actionCalls.addAll(othersActionCall);

                return actionCalls;
            case SEMICOLON:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {COMMA, SEMICOLON};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private ActionCallExpression parseActionCall() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                String name = match(IDENTIFIER); //TODO check exist
                match(LPAREN);
                List<Expression> parameters = parseListExpr();
                match(RPAREN);
                String callName = Method.createTypedName(name, parameters);
                Declaration call = symbolTable.getGlobalDeclaration(callName);
                if (call == null)
                    throw new SemanticException(SemanticException.NO_EXISTE_ACCION, nextToken);
                else if (call instanceof Action)
                    return new ActionCallExpression( (Action) call, new CallParameters(parameters));
                else
                    throw new SemanticException(SemanticException.WRONG_SIGNATURE, nextToken);
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Expression> parseListExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Expression firstExpression = parseExpr();
                List<Expression> othersExpressions = parseMoreExpr();

                List<Expression> expressions = new LinkedList<Expression>();
                expressions.add(firstExpression);
                expressions.addAll(othersExpressions);

                return expressions;
            case RPAREN:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected  = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18, RPAREN
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private List<Expression> parseMoreExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                Expression firstExpression = parseExpr();
                List<Expression> othersExpressions = parseMoreExpr();

                List<Expression> expressions = new LinkedList<Expression>();
                expressions.add(firstExpression);
                expressions.addAll(othersExpressions);

                return expressions;
            case RPAREN:
                return Collections.EMPTY_LIST;
            default: {
                int[] expected = {COMMA, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Expression firstExpression = parseAndExpr();

                return parseListOrExpr(firstExpression);
            default: {
                int[] expected = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseListOrExpr(Expression firsExpression) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case OR:
                match(OR);
                Expression secondExpression = parseAndExpr();

                if(firsExpression.getType() != Type.BOOLEAN_TYPE || secondExpression.getType() != Type.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                Expression newFirst = new BinaryExpression(Type.BOOLEAN_TYPE, BinaryExpression.OR, firsExpression, secondExpression);

                return parseListOrExpr(newFirst);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
                return firsExpression;
            default: {
                int[] expected = {OR, RPAREN, SEMICOLON, ARROW, COMMA};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseAndExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Expression firstExpression = parseRelExpr();
                return parseListAndExpr(firstExpression);

            default: {
                int[] expected = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseListAndExpr(Expression firstExpression) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case AND:
                match(AND);
                Expression secondExpression = parseRelExpr();

                if(firstExpression.getType() != Type.BOOLEAN_TYPE || secondExpression.getType() != Type.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                Expression newFirst = new BinaryExpression(Type.BOOLEAN_TYPE, BinaryExpression.AND, firstExpression, secondExpression);

                return parseListAndExpr(newFirst);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
                return firstExpression;
            default: {
                int[] expected = {AND, RPAREN, SEMICOLON, ARROW, COMMA, OR};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseRelExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Expression firstExpression = parseSumExpr();

                return parseOptionalRelOp(firstExpression);
            default: {
                int[] expected = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseOptionalRelOp(Expression firsExpression) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE:
                int relOp = parseRelOp();
                Expression secondExpression = parseSumExpr();

                if (firsExpression.getType() == Type.INT_TYPE || firsExpression.getType() == Type.DOUBLE_TYPE) {
                    if (secondExpression.getType() == Type.INT_TYPE || secondExpression.getType() == Type.DOUBLE_TYPE) {
                    }//all ok
                    else throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                } else if(firsExpression.getType() != secondExpression.getType())
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                return new BinaryExpression(Type.BOOLEAN_TYPE, relOp, firsExpression, secondExpression);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
            case AND:
                return firsExpression;
            default: {
                int[] expected = {EQ, NE, GT, GE, LT, LE, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private int parseRelOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case EQ:
                match(EQ);
                return BinaryExpression.EQ;
            case NE:
                match(NE);
                return BinaryExpression.NEQ;
            case GT:
                match(GT);
                return BinaryExpression.GT;
            case GE:
                match(GE);
                return BinaryExpression.GE;
            case LT:
                match(LT);
                return BinaryExpression.LT;
            case LE:
                match(LE);
                return BinaryExpression.LE;
            default: {
                int[] expected = {EQ, NE, GT, GE, LT, LE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseSumExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case NOT:
            case MINUS:
            case PLUS:
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                int unOp = parseUnOP();
                Expression firstExpressionSinOP = parseProdExpr();

                if (unOp == UnaryExpression.NOT && firstExpressionSinOP.getType() != Type.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);
                if (unOp == UnaryExpression.MINUS || unOp == UnaryExpression.PLUS)
                    if (firstExpressionSinOP.getType() != Type.INT_TYPE &&  firstExpressionSinOP.getType() != Type.DOUBLE_TYPE)
                        throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                Expression firstExpression = new UnaryExpression(firstExpressionSinOP.getType(), unOp, firstExpressionSinOP); //TODO chek tipos operandos

                return parseListSumOp(firstExpression);
            default: {
                int[] expected  = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseListSumOp(Expression firstExpression) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case PLUS:
            case MINUS:
                int sumOp = parseSumOp();
                int resultType;

                Expression secondExpression = parseProdExpr();

                if (firstExpression.getType() == Type.BOOLEAN_TYPE || secondExpression.getType() == Type.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                resultType = Math.max(firstExpression.getType(), secondExpression.getType()); //int 0 double 1
                Expression newFirst = new BinaryExpression(resultType, sumOp, firstExpression, secondExpression);
                return parseListSumOp(newFirst);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
            case AND:
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE:
                return firstExpression;
            default: {
                int[] expected = {PLUS, MINUS, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND,
                        EQ, NE, GT, GE, LT, LE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private int parseUnOP() throws SintaxException{
        switch (nextToken.getKind()) {
            case NOT:
                match(NOT);
                return UnaryExpression.NOT;
            case MINUS:
                match(MINUS);
                return UnaryExpression.MINUS;
            case PLUS:
                match(PLUS);
                return UnaryExpression.PLUS;
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                return UnaryExpression.NONE;
            default: {
                int[] expected  = {NOT, MINUS, PLUS, INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private int parseSumOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case MINUS:
                match(MINUS);
                return BinaryExpression.MINUS;
            case PLUS:
                match(PLUS);
                return BinaryExpression.PLUS;
            default: {
                int[] expected = {MINUS, PLUS};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseProdExpr() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
            case IDENTIFIER:
            case LPAREN:
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                Expression firstExpression = parseFactor();
                return parseListFactor(firstExpression);
            default: {
                int[] expected  = {INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseListFactor(Expression firstExpression) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case PROD:
            case DIV:
            case MOD:
                int multOp = parseMultOp();
                Expression secondExpression = parseFactor();

                if (firstExpression.getType() == Type.BOOLEAN_TYPE || secondExpression.getType() == Type.BOOLEAN_TYPE)
                    throw new SemanticException(SemanticException.TYPE_MISSMATCH, nextToken);

                int resultType;
                resultType = Math.max(firstExpression.getType(), secondExpression.getType()); //int 0 double 1

                Expression newFirst;
                if (multOp == BinaryExpression.MOD)
                    newFirst = new BinaryExpression(Type.INT_TYPE, multOp, firstExpression, secondExpression);
                else
                    newFirst = new BinaryExpression(resultType, multOp, firstExpression, secondExpression);

                return parseListFactor(newFirst);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
            case AND:
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE:
            case PLUS:
            case MINUS:
                return firstExpression;
            default: {
                int[] expected = {PROD, DIV, MOD, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND, EQ, NE,
                        GT, GE, LT, LE, PLUS, MINUS};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private int parseMultOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case PROD:
                match(PROD);
                return BinaryExpression.PROD;
            case DIV:
                match(DIV);
                return BinaryExpression.DIV;
            case MOD:
                match(MOD);
                return BinaryExpression.MOD;
            default: {
                int[] expected = {PROD, DIV, MOD};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseFactor() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
                return parseLiteral();
            case GEAR:
            case SPEED:
            case ANGLE:
            case POSITION:
            case RPM:
            case SENSOR_0:
            case SENSOR_1:
            case SENSOR_2:
            case SENSOR_3:
            case SENSOR_4:
            case SENSOR_5:
            case SENSOR_6:
            case SENSOR_7:
            case SENSOR_8:
            case SENSOR_9:
            case SENSOR_10:
            case SENSOR_11:
            case SENSOR_12:
            case SENSOR_13:
            case SENSOR_14:
            case SENSOR_15:
            case SENSOR_16:
            case SENSOR_17:
            case SENSOR_18:
                return new VariableExpression(parseInputVar());
            case IDENTIFIER:
                return parseReference();
            case LPAREN:
                match(LPAREN);
                Expression expression = parseExpr();
                match(RPAREN);
                return expression;
            default: {
                int[] expected  = {INTEGER_LITERAL, DOUBLE_LITERAL, TRUE, FALSE, IDENTIFIER,
                        LPAREN, GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2, SENSOR_3,
                        SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9, SENSOR_10, SENSOR_11,
                        SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16, SENSOR_17, SENSOR_18
                };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Literal parseLiteral() throws SintaxException{
        switch (nextToken.getKind()) {
            case INTEGER_LITERAL:
                String lexInt = match(INTEGER_LITERAL);
                return new IntegerLiteralExpression(lexInt);
            case DOUBLE_LITERAL:
                String lexDo = match(DOUBLE_LITERAL);
                return new DoubleLiteralExpression(lexDo);
            case TRUE:
                match(TRUE);
                return new BooleanLiteralExpression(true);
            case FALSE:
                match(FALSE);
                return new BooleanLiteralExpression(false);
            default: {
                int[] expected = {DOUBLE_LITERAL, INTEGER_LITERAL, TRUE, FALSE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseReference() throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                String name = match(IDENTIFIER);
                return parseOptMethodCall(name);
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseOptMethodCall(String name) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LPAREN:
                return parseMethodCall(name);
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
            case AND:
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE:
            case PLUS:
            case MINUS:
            case PROD:
            case DIV:
            case MOD:
                Variable var = symbolTable.getVariable(name);
                if (var == null) throw new SemanticException(SemanticException.VAR_NOT_DECLARED, nextToken);
                else return new VariableExpression(var);
            default: {
                int[] expected = {LPAREN, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND, EQ, NE,
                        GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private Expression parseMethodCall(String name) throws SintaxException, SemanticException {
        switch (nextToken.getKind()) {
            case LPAREN:
                match(LPAREN);
                List<Expression> parameters = parseListExpr();
                match(RPAREN);

                String callName = Method.createTypedName(name, parameters);
                Declaration call = symbolTable.getGlobalDeclaration(callName);

                if (call == null)
                    throw new SemanticException(SemanticException.NO_EXISTE_PERCEPCION, nextToken);
                else if (call instanceof Perception)
                    return new PerceptionCallExpression( (Perception) call, new CallParameters(parameters));
                else
                    throw new SemanticException(SemanticException.WRONG_SIGNATURE, nextToken);

            default: {
                int[] expected = {LPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private static final boolean readOnly = true;



    public Token getNextToken() {
        return nextToken;
    }

}
