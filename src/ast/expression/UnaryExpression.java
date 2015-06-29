package ast.expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class UnaryExpression extends Expression {


    private final Expression expression;
    private final int op;

    public UnaryExpression(int type, int op, Expression expression) {
        super(type);
        this.op = op;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getOp() {
        return op;
    }

    @Override
    public String genJavaCode() {
        return getJavaOp() +" "+ expression.genJavaCode();
    }

    public String getJavaOp() {
        switch (op) {
            case NONE:
                return "";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case NOT:
                return "!";
        }
        return null;
    }

    /**
     * Código para indicar que no hay operador unario
     */
    public static final int NONE = 0;

    /**
     * Operador: PLUS
     */
    public static final int PLUS = 1;

    /**
     * Operador: MINUS
     */
    public static final int MINUS = 2;

    /**
     * Operador: NOT
     */
    public static final int NOT = 3;
}
