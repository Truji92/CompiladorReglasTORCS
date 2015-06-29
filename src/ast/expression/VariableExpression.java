package ast.expression;

import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class VariableExpression extends Expression{

    private final Variable var;

    public VariableExpression(Variable var) {
        super(var.getType());
        this.var = var;
    }

    public Variable getVar() {
        return var;
    }
}
