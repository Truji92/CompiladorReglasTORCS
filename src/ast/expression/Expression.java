package ast.expression;

import ast.Type;

/**
 * Created by alejandro on 28/06/15.
 */
public abstract class Expression implements Type{

    private final int type;

    public Expression(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String genJavaCode(){ return "ExPRESIon";}
}
