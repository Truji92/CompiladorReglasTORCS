package ast.expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class BooleanLiteralExpression extends Literal {


    private final boolean value;

    public BooleanLiteralExpression(String lexeme) {
        super(BOOLEAN_TYPE);
        this.value = Boolean.parseBoolean(lexeme);
    }

    public BooleanLiteralExpression(boolean value) {
        super(BOOLEAN_TYPE);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String genJavaCode() {
        if (value) return "true";
        else return "false";
    }
}
