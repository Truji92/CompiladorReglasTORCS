package ast.structs;

import ast.expression.ActionCallExpression;
import ast.expression.Expression;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Rule {
    private Expression expression;
    private List<ActionCallExpression> actions;

    public Rule(Expression expression, List<ActionCallExpression> actions) {
        this.expression = expression;
        this.actions = actions;
    }
}
