package ast.statements;

import ast.expression.Expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptWhileStm extends PcptStatement{

    private Expression condition;
    private PcptStatement code;

    public PcptWhileStm(Expression condition, PcptStatement code) {
        super(VOID_TYPE);
        this.condition = condition;
        this.code = code;
    }

    public Expression getCondition() {
        return condition;
    }

    public PcptStatement getCode() {
        return code;
    }
}
