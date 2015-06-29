package ast.expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class IntegerLiteralExpression extends Literal {

    private final int value;

    public IntegerLiteralExpression(String lexeme) {
        super(INT_TYPE);
        this.value = Integer.parseInt(lexeme);
    }

    public IntegerLiteralExpression(int value) {
        super(INT_TYPE);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
