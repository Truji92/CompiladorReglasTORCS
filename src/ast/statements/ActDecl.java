package ast.statements;

import ast.expression.Expression;
import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActDecl extends ActStatement{
    private Variable var;
    private Expression assign;
    private boolean declarationPlusAssign;

    public ActDecl(Variable var, Expression assign) {
        super(var.getType());
        this.var = var;
        this.assign = assign;
        declarationPlusAssign = true;
    }

    public ActDecl(Variable var) {
        super(var.getType());
        this.var = var;
        declarationPlusAssign = false;
    }
}
