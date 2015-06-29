package ast.statements;

import ast.expression.Expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptIfStm extends PcptStatement {

    private Expression condition;

    private PcptStatement thenStm;
    private PcptStatement elseStm;

    public PcptIfStm(Expression condition, PcptStatement thenStm, PcptStatement elseStm) {
        super(VOID_TYPE);
        this.condition = condition;
        this.thenStm = thenStm;
        this.elseStm = elseStm;
    }

    public Expression getCondition() {
        return condition;
    }

    public PcptStatement getThenStm() {
        return thenStm;
    }

    public PcptStatement getElseStm() {
        return elseStm;
    }
}
