package ast.expression;

import ast.structs.Perception;

/**
 * Created by alejandro on 28/06/15.
 */
public class PerceptionCallExpression extends Expression{

    private final Perception perception;
    private final CallParameters parameters;

    public PerceptionCallExpression(Perception perception, CallParameters parameters) {
        super(BOOLEAN_TYPE);
        this.perception = perception;
        this.parameters = parameters;
    }

    public Perception getPerception() {
        return perception;
    }

    public CallParameters getParameters() {
        return parameters;
    }

    @Override
    public String genJavaCode() {
        return perception.getName() + "(sensors" + parameters.genJavaCode() +")";
    }
}
