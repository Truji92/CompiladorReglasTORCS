package ast.structs;

import ast.expression.Literal;

/**
 * Created by alejandro on 28/06/15.
 */
public class InnerDecl extends Declaration {

    private final Variable var;
    private final Literal assign;

    public InnerDecl(Variable var, Literal lit) {
        this.var = var;
        this.assign = lit;
    }

    public Variable getVar() {
        return var;
    }

    public Literal getAssign() {
        return assign;
    }

    @Override
    public String genJavaCode() {

        return  "private " + var.genJavaCode() + " = " + assign.genJavaCode() + ";";
    }
}
