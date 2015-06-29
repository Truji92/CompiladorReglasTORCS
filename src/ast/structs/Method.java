package ast.structs;

import parser.SemanticException;
import ast.Type;
import ast.expression.Expression;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Method extends Declaration implements Type {
    protected final String name;
    protected final List<Variable> arguments;
    protected final List<Variable> localVar;
    protected final SymbolTable symboltable;

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

    public String getName() {
        return name;
    }

    public int[] getTypes() {
        int[] types = new int[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            types[i] = arguments.get(i).getType();
        }
        return types;
    }

    public String getTypedName() {
        String typedName = "";
        typedName = typedName + name;
        int[] types = getTypes();
        for (int i = 0; i < types.length; i++) {
            typedName += "_"+type2String(types[i]);
        }
        return typedName;
    }

    public static int[] extractTypes(List<Expression> args) {
        int[] types = new int[args.size()];
        for (int i = 0; i < args.size(); i++) {
            types[i] = args.get(i).getType();
        }
        return types;
    }

    public static String createTypedName(String name, List<Expression> args) {
        String typedName = "";
        typedName = typedName + name;
        int[] types = extractTypes(args);
        for (int i = 0; i < types.length; i++) {
            typedName += "_"+type2String(types[i]);
        }
        return typedName;
    }

    private static String type2String(int type) {
        switch (type) {
            case BOOLEAN_TYPE:
                return "boolean";
            case INT_TYPE:
                return "int";
            case DOUBLE_TYPE:
                return "double";
            case VOID_TYPE:
                return "void";
            default:
                return "";
        }
    }
}
