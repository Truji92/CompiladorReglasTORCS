package ast.statements;

import ast.expression.Expression;
import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActAssignStm extends ActStatement {

    private Variable var;
    private Expression assign;


    public ActAssignStm(Variable var, Expression assign) {
        super(VOID_TYPE);
        this.var = var;
        this.assign = assign;
    }

    public Variable getVar() {
        return var;
    }

    public Expression getAssign() {
        return assign;
    }
}
