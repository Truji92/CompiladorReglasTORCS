package ast.structs;

import ast.SemanticException;
import ast.Type;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Method extends Declaration implements Type {
    protected String name;
    protected List<Variable> arguments;
    protected List<Variable> localVar;
    protected SymbolTable symboltable;

    public Method(SymbolTable symboltable, String name) {
        localVar = new LinkedList<Variable>();
        this.symboltable = symboltable;
        this.name = name;
        arguments = new LinkedList<Variable>();
    }

    public void addArgument(Variable argument) throws SemanticException {
        arguments.add(argument);
        symboltable.addVariable(argument);
    }

    public void addLocalVariable(Variable local) throws SemanticException {
        localVar.add(local);
        symboltable.addVariable(local);
    }
}
