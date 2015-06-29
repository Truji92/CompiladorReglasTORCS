package ast.expression;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class CallParameters  {

    private final List<Expression> parameters;

    public CallParameters(List<Expression> parameters) {
        this.parameters = parameters;
    }

    public CallParameters() {
        this.parameters = new LinkedList<Expression>();
    }

    public List<Expression> getParameters() {
        return parameters;
    }

    public void addParameter(Expression parameter) {
        parameters.add(parameter);
    }

    public int[] getTypes() {
        int[] types = new int[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            types[i] = parameters.get(i).getType();
        }
        return types;
    }

    public String genJavaCode() {
        String javaCode = "";
        for (Expression parameter: parameters) {
            javaCode += ", " + parameter.genJavaCode();
        }
        return javaCode;
    }
}
