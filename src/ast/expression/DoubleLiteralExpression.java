package ast.expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class DoubleLiteralExpression extends Literal {

    private final double value;

    public DoubleLiteralExpression(String lexeme) {
        super(DOUBLE_TYPE);
        this.value = Double.parseDouble(lexeme);
    }

    public DoubleLiteralExpression(Double value) {
        super(DOUBLE_TYPE);
        this.value = value;
    }
    public double getValue() {
        return value;
    }
}
