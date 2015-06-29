package ast.structs;

import ast.Type;

/**
 * Created by alejandro on 28/06/15.
 */
public class Variable implements Type {
    public final String name;
    private int type;
    private boolean readOnly;

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
            if (expType == INT_TYPE || expType == DOUBLE_TYPE)
                return true;
            else return false;
        }
        else return varType == expType;
    }
}
