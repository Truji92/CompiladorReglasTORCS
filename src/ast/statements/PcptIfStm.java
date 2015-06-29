package ast.statements;

import ast.expression.Expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptIfStm extends PcptStatement {

    private final Expression condition;

    private final PcptStatement thenStm;
    private final PcptStatement elseStm;

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
