package ast.expression;

import ast.structs.Action;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActionCallExpression extends Expression {

    private final Action action;
    private final CallParameters parameters;

    public ActionCallExpression(Action action, CallParameters parameters) {
        super(VOID_TYPE);
        this.action = action;
        this.parameters = parameters;
    }

    public Action getAction() {
        return action;
    }

    public CallParameters getParameters() {
        return parameters;
    }
}
