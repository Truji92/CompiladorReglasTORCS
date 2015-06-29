package ast.structs;

import ast.Type;

/**
 * Created by alejandro on 28/06/15.
 */
public class Variable implements Type {
    public final String name;
    private int type;
    private final boolean readOnly;

    public Variable(String name, int type, boolean readOnly) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean equals(String id) {
        return name.equals(id);
    }

    public static boolean checkMatchingType(int varType, int expType) {
        if (varType == DOUBLE_TYPE){
            return expType == INT_TYPE || expType == DOUBLE_TYPE;
        }
        else return varType == expType;
    }

    public String genJavaCode() {
        return getTypeAsString() + " " + name;
    }

    public String genJavaCodeRef() { return name;}

    private String getTypeAsString() {
        switch (type) {
            case INT_TYPE:
                return "int";
            case BOOLEAN_TYPE:
                return "boolean";
            case DOUBLE_TYPE:
                return "double";
        }
        return "";
    }
}
