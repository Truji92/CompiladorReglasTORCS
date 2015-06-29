package ast.structs;

import ast.expression.ActionCallExpression;
import ast.expression.Expression;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Rule {
    private final Expression expression;
    private final List<ActionCallExpression> actions;

    public Rule(Expression expression, List<ActionCallExpression> actions) {
        this.expression = expression;
        this.actions = actions;
    }

    public String genJavaCode() {
        String javaCode = "if (";
        javaCode += expression.genJavaCode() + ") {\n";
        for (ActionCallExpression action: actions) {
            javaCode += "\t\t\t"+action.genJavaCode() + "\n";
        }
        javaCode += "\t\t}\n";
        return javaCode;
    }
}
