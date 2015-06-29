package ast.statements;

import ast.expression.Expression;
import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptDecl extends PcptStatement {

    private Variable var;
    private Expression assign;
    private boolean declarationPlusAssign;

    public PcptDecl(Variable var, Expression assign) {
        super(var.getType());
        this.var = var;
        this.assign = assign;
        declarationPlusAssign = true;
    }

    public PcptDecl(Variable var) {
        super(var.getType());
        this.var = var;
        declarationPlusAssign = false;
    }
}
