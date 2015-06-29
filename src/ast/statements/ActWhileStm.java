package ast.statements;

import ast.expression.Expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActWhileStm extends ActStatement {

    private final Expression condition;
    private final ActStatement code;

    public ActWhileStm(Expression condition, ActStatement code) {
        super(VOID_TYPE);
        this.condition = condition;
        this.code = code;
    }

    public Expression getCondition() {
        return condition;
    }

    public ActStatement getCode() {
        return code;
    }
}
