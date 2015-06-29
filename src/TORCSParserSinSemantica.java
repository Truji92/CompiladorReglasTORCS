//------------------------------------------------------------------//
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


/**
 * Analizador sint�ctico de Tinto basado en una gram�tica BNF y LL(1)
 *  
 * @author Francisco Jos� Moreno Velo
 *
 */
public class TORCSParserSinSemantica implements TokenConstants {

	/**
	 * Analizador l�xico
	 */
	private TORCSLexer lexer;

	/**
	 * Siguiente token de la cadena de entrada
	 */
	private Token nextToken;

	/**
	 * M�todo de an�lisis de un fichero
	 * 
	 * @param filename Nombre del fichero a analizar
	 * @return Resultado del an�lisis sint�ctico
	 */
	public boolean parse(String filename) {
		try {
			this.lexer = new TORCSLexer(filename);
			this.nextToken = lexer.getNextToken();
			Controller();
			if(nextToken.getKind() == EOF) return true;
			else return false;
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return false;
		}
	}

	/**
	 * Punto de entrada para ejecutar pruebas del analizador sint�ctico
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		if(args.length == 0) return;

		TORCSParser parser = new TORCSParser();
		if(parser.parse(args[0])) {
			System.out.println("Correcto");
		} else {
			System.out.println("Incorrecto");
		}
	}

	/**
	 * M�todo que consume un token de la cadena de entrada
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token 
	 */
	private void match(int kind) throws SintaxException {
		if(nextToken.getKind() == kind) nextToken = lexer.getNextToken();
		else throw new SintaxException(nextToken,kind);
	}

    private void Controller() throws SintaxException{
        switch (nextToken.getKind()) {
            case INNER:
            case PERCEPTION:
            case ACTION:
            case RULES:
                ListDeclaration();
                Rules();
                break;
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION, RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListDeclaration() throws SintaxException{
        switch (nextToken.getKind()) {
            case INNER:
            case PERCEPTION:
            case ACTION:
                Declaration();
                
                ListDeclaration();
                break;
            case RULES:
                break;
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION, RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Declaration() throws SintaxException{
        switch (nextToken.getKind()) {
            case INNER:
                InnerDecl();
                break;
            case PERCEPTION:
                PerceptionDecl();
                break;
            case ACTION:
                ActionDecl();
                break;
            default: {
                int[] expected = {INNER, PERCEPTION, ACTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void InnerDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case INNER:
                match(INNER);
                Type();
                match(IDENTIFIER);
                match(ASSIGN);
                Literal();
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {INNER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Type() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
                match(INT);
                break;
            case BOOLEAN:
                match(BOOLEAN);
                break;
            case DOUBLE:
                match(DOUBLE);
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PerceptionDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case PERCEPTION:
                match(PERCEPTION);
                match(IDENTIFIER);
                ArgumentDecl();
                PerceptionBody();
                break;
            default: {
                int[] expected = {PERCEPTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActionDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case ACTION:
                match(ACTION);
                match(IDENTIFIER);
                ArgumentDecl();
                ActionBody();
                break;
            default: {
                int[] expected = {ACTION};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ArgumentDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case LPAREN:
                match(LPAREN);
                ListArguments();
                match(RPAREN);
                break;
            default: {
                int[] expected = {LPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListArguments() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                Argument();
                MoreArguments();
                break;
            case RPAREN:
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void MoreArguments() throws SintaxException{
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                Argument();
                MoreArguments();
                break;
            case RPAREN:
                break;
            default: {
                int[] expected = {COMMA, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Argument() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                Type();
                match(IDENTIFIER);
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PerceptionBody() throws SintaxException{
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                ListPcptStatement();
                match(RBRACE);
                break;
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListPcptStatement() throws SintaxException{
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
                PcptStatement();
                ListPcptStatement();
                break;
            case RBRACE:
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE, LBRACE, RBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActionBody() throws SintaxException{
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                ListActStatement();
                match(RBRACE);
                break;
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListActStatement() throws SintaxException{
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
                ActStatement();
                ListActStatement();
                break;
            case RBRACE:
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, LBRACE, GEAR,
                        ACCELERATE, BRAKE, STEERING, RBRACE };
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Rules() throws SintaxException{
        switch (nextToken.getKind()) {
            case RULES:
                match(RULES);
                match(LBRACE);
                ListRules();
                match(RBRACE);
                break;
            default: {
                int[] expected = {RULES};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListRules() throws SintaxException{
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
                Rule();
                ListRules();
                break;
            case RBRACE:
                break;
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

    private void InputVar() throws SintaxException{
        switch (nextToken.getKind()) {
            case GEAR:
                match(GEAR);
                break;
            case SPEED:
                match(SPEED);
                break;
            case ANGLE:
                match(ANGLE);
                break;
            case POSITION:
                match(POSITION);
                break;
            case RPM:
                match(RPM);
                break;
            case SENSOR_0:
                match(SENSOR_0);
                break;
            case SENSOR_1:
                match(SENSOR_1);
                break;
            case SENSOR_2:
                match(SENSOR_2);
                break;
            case SENSOR_3:
                match(SENSOR_3);
                break;
            case SENSOR_4:
                match(SENSOR_4);
                break;
            case SENSOR_5:
                match(SENSOR_5);
                break;
            case SENSOR_6:
                match(SENSOR_6);
                break;
            case SENSOR_7:
                match(SENSOR_7);
                break;
            case SENSOR_8:
                match(SENSOR_8);
                break;
            case SENSOR_9:
                match(SENSOR_9);
                break;
            case SENSOR_10:
                match(SENSOR_10);
                break;
            case SENSOR_11:
                match(SENSOR_11);
                break;
            case SENSOR_12:
                match(SENSOR_12);
                break;
            case SENSOR_13:
                match(SENSOR_13);
                break;
            case SENSOR_14:
                match(SENSOR_14);
                break;
            case SENSOR_15:
                match(SENSOR_15);
                break;
            case SENSOR_16:
                match(SENSOR_16);
                break;
            case SENSOR_17:
                match(SENSOR_17);
                break;
            case SENSOR_18:
                match(SENSOR_18);
                break;
            default: {
                int[] expected = {GEAR, SPEED, ANGLE, POSITION, RPM, SENSOR_0, SENSOR_1, SENSOR_2,
                        SENSOR_3, SENSOR_4, SENSOR_5, SENSOR_6, SENSOR_7, SENSOR_8, SENSOR_9,
                        SENSOR_10, SENSOR_11, SENSOR_12, SENSOR_13, SENSOR_14, SENSOR_15, SENSOR_16,
                        SENSOR_17, SENSOR_18};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void OutputVar() throws SintaxException{
        switch (nextToken.getKind()) {
            case GEAR:
                match(GEAR);
                break;
            case ACCELERATE:
                match(ACCELERATE);
                break;
            case BRAKE:
                match(BRAKE);
                break;
            case STEERING:
                match(STEERING);
                break;
            default: {
                int[] expected = {GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptStatement() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                PcptDecl();
                break;
            case IDENTIFIER:
                PcptAssignStm();
                break;
            case IF:
                PcptIfStm();
                break;
            case WHILE:
                PcptWhileStm();
                break;
            case TRUE:
                PcptTrueStm();
                break;
            case FALSE:
                PcptFalseStm();
                break;
            case LBRACE:
                PcptBlockStm();
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE, LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                Type();
                match(IDENTIFIER);
                OptAssign();
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void OptAssign() throws SintaxException{
        switch (nextToken.getKind()) {
            case ASSIGN:
                match(ASSIGN);
                Expr();
                break;
            case SEMICOLON:
                break;
            default: {
                int[] expected = {ASSIGN, SEMICOLON};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptIfStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case IF:
                match(IF);
                match(LPAREN);
                Expr();
                match(RPAREN);
                PcptStatement();
                PcptElse();
                break;
            default: {
                int[] expected = {IF};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptElse() throws SintaxException{
        switch (nextToken.getKind()) {
            case ELSE:
                match(ELSE);
                PcptStatement();
                break;
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
                break;
            default: {
                int[] expected = {ELSE, INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, TRUE, FALSE,
                        LBRACE, RBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptWhileStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case WHILE:
                match(WHILE);
                match(LPAREN);
                Expr();
                match(RPAREN);
                PcptStatement();
                break;
            default: {
                int[] expected = {WHILE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptAssignStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                match(IDENTIFIER);
                match(ASSIGN);
                Expr();
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptTrueStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case TRUE:
                match(TRUE);
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {TRUE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptFalseStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case FALSE:
                match(FALSE);
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {FALSE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void PcptBlockStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                ListPcptStatement();
                match(RBRACE);
                break;
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActStatement() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                ActDecl();
                break;
            case IDENTIFIER:
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                ActAssignStm();
                break;
            case IF:
                ActIfStm();
                break;
            case WHILE:
                ActWhileStm();
                break;
            case LBRACE:
                ActBlockStm();
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE, IDENTIFIER, GEAR, ACCELERATE, BRAKE, STEERING,
                        IF, WHILE, LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActDecl() throws SintaxException{
        switch (nextToken.getKind()) {
            case INT:
            case BOOLEAN:
            case DOUBLE:
                Type();
                match(IDENTIFIER);
                OptAssign();
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {INT, BOOLEAN, DOUBLE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActIfStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case IF:
                match(IF);
                match(LPAREN);
                Expr();
                match(RPAREN);
                ActStatement();
                ActElse();
                break;
            default: {
                int[] expected = {IF};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActElse() throws SintaxException{
        switch (nextToken.getKind()) {
            case ELSE:
                match(ELSE);
                ActStatement();
                break;
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
                break;
            default: {
                int[] expected = {ELSE, INT, BOOLEAN, DOUBLE, IDENTIFIER, IF, WHILE, LBRACE, RBRACE,
                        GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActWhileStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case WHILE:
                match(WHILE);
                match(LPAREN);
                Expr();
                match(RPAREN);
                ActStatement();
                break;
            default: {
                int[] expected = {WHILE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActAssignStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                match(IDENTIFIER);
                match(ASSIGN);
                Expr();
                match(SEMICOLON);
                break;
            case GEAR:
            case ACCELERATE:
            case BRAKE:
            case STEERING:
                OutputVar();
                match(ASSIGN);
                Expr();
                match(SEMICOLON);
                break;
            default: {
                int[] expected = {IDENTIFIER, GEAR, ACCELERATE, BRAKE, STEERING};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActBlockStm() throws SintaxException{
        switch (nextToken.getKind()) {
            case LBRACE:
                match(LBRACE);
                ListActStatement();
                match(RBRACE);
                break;
            default: {
                int[] expected = {LBRACE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Rule() throws SintaxException{
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
                Expr();
                match(ARROW);
                ActionCall();
                MoreActionCall();
                match(SEMICOLON);
                break;
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

    private void MoreActionCall() throws SintaxException{
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                ActionCall();
                MoreActionCall();
                break;
            case SEMICOLON:
                break;
            default: {
                int[] expected = {COMMA, SEMICOLON};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ActionCall() throws SintaxException{
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                match(IDENTIFIER);
                match(LPAREN);
                ListExpr();
                match(RPAREN);
                break;
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ListExpr() throws SintaxException{
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
                Expr();
                MoreExpr();
                break;
            case RPAREN:
                break;
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

    private void MoreExpr() throws SintaxException{
        switch (nextToken.getKind()) {
            case COMMA:
                match(COMMA);
                Expr();
                MoreExpr();
                break;
            case RPAREN:
                break;
            default: {
                int[] expected = {COMMA, RPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Expr() throws SintaxException{
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
                AndExpr();
                ListOrExpr();
                break;
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

    private void ListOrExpr() throws SintaxException{
        switch (nextToken.getKind()) {
            case OR:
                match(OR);
                AndExpr();
                ListOrExpr();
                break;
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
                break;
            default: {
                int[] expected = {OR, RPAREN, SEMICOLON, ARROW, COMMA};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void AndExpr() throws SintaxException{
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
                RelExpr();
                ListAndExpr();
                break;
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

    private void ListAndExpr() throws SintaxException{
        switch (nextToken.getKind()) {
            case AND:
                match(AND);
                RelExpr();
                ListAndExpr();
                break;
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
                break;
            default: {
                int[] expected = {AND, RPAREN, SEMICOLON, ARROW, COMMA, OR};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void RelExpr() throws SintaxException{
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
                SumExpr();
                OptionalRelOp();
                break;
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

    private void OptionalRelOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE:
                RelOp();
                SumExpr();
                break;
            case RPAREN:
            case SEMICOLON:
            case ARROW:
            case COMMA:
            case OR:
            case AND:
                break;
            default: {
                int[] expected = {EQ, NE, GT, GE, LT, LE, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void RelOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case EQ:
                match(EQ);
                break;
            case NE:
                match(NE);
                break;
            case GT:
                match(GT);
                break;
            case GE:
                match(GE);
                break;
            case LT:
                match(LT);
                break;
            case LE:
                match(LE);
                break;
            default: {
                int[] expected = {EQ, NE, GT, GE, LT, LE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void SumExpr() throws SintaxException{
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
                UnOP();
                ProdExpr();
                ListSumOp();
                break;
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

    private void ListSumOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case PLUS:
            case MINUS:
                SumOp();
                ProdExpr();
                ListSumOp();
                break;
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
                break;
            default: {
                int[] expected = {PLUS, MINUS, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND,
                        EQ, NE, GT, GE, LT, LE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void UnOP() throws SintaxException{
        switch (nextToken.getKind()) {
            case NOT:
                match(NOT);
                break;
            case MINUS:
                match(MINUS);
                break;
            case PLUS:
                match(PLUS);
                break;
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
                break;
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

    private void SumOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case MINUS:
                match(MINUS);
                break;
            case PLUS:
                match(PLUS);
                break;
            default: {
                int[] expected = {MINUS, PLUS};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void ProdExpr() throws SintaxException{
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
                Factor();
                ListFactor();
                break;
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

    private void ListFactor() throws SintaxException{
        switch (nextToken.getKind()) {
            case PROD:
            case DIV:
            case MOD:
                MultOp();
                Factor();
                ListFactor();
                break;
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
                break;
            default: {
                int[] expected = {PROD, DIV, MOD, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND, EQ, NE,
                        GT, GE, LT, LE, PLUS, MINUS};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void MultOp() throws SintaxException{
        switch (nextToken.getKind()) {
            case PROD:
                match(PROD);
                break;
            case DIV:
                match(DIV);
                break;
            case MOD:
                match(MOD);
                break;
            default: {
                int[] expected = {PROD, DIV, MOD};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Factor() throws SintaxException{
        switch (nextToken.getKind()) {
            case INTEGER_LITERAL:
            case DOUBLE_LITERAL:
            case TRUE:
            case FALSE:
                Literal();
                break;
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
                InputVar();
                break;
            case IDENTIFIER:
                Reference();
                break;
            case LPAREN:
                match(LPAREN);
                Expr();
                match(RPAREN);
                break;
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

    private void Literal() throws SintaxException{
        switch (nextToken.getKind()) {
            case INTEGER_LITERAL:
                match(INTEGER_LITERAL);
                break;
            case DOUBLE_LITERAL:
                match(DOUBLE_LITERAL);
                break;
            case TRUE:
                match(TRUE);
                break;
            case FALSE:
                match(FALSE);
                break;
            default: {
                int[] expected = {DOUBLE_LITERAL, INTEGER_LITERAL, TRUE, FALSE};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void Reference() throws SintaxException{
        switch (nextToken.getKind()) {
            case IDENTIFIER:
                match(IDENTIFIER);
                OptMethodCall();
                break;
            default: {
                int[] expected = {IDENTIFIER};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void OptMethodCall() throws SintaxException{
        switch (nextToken.getKind()) {
            case LPAREN:
                MethodCall();
                break;
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
                break;
            default: {
                int[] expected = {LPAREN, RPAREN, SEMICOLON, ARROW, COMMA, OR, AND, EQ, NE,
                        GT, GE, LT, LE, PLUS, MINUS, PROD, DIV, MOD};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

    private void MethodCall() throws SintaxException{
        switch (nextToken.getKind()) {
            case LPAREN:
                match(LPAREN);
                ListExpr();
                match(RPAREN);
                break;
            default: {
                int[] expected = {LPAREN};
                throw new SintaxException(nextToken, expected);
            }
        }
    }

}
