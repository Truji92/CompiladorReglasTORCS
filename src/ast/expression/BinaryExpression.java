package ast.expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class BinaryExpression extends Expression {

    private final int operator;
    private final Expression left;
    private final Expression right;

    public BinaryExpression(int type, int operator, Expression left, Expression right) {
        super(type);
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public int getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }


    @Override
    public String genJavaCode() {
        return left.genJavaCode() +" "+ getJavaOperator() +" "+right.genJavaCode();
    }

    private String getJavaOperator() {
        switch (operator){
            case AND:
                return "&&";
            case OR:
                return "||";
            case PROD:
                return "*";
            case DIV:
                return "/";
            case MOD:
                return "%";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case EQ:
                return "==";
            case NEQ:
                return "!=";
            case GT:
                return ">";
            case LT:
                return "<";
            case GE:
                return ">=";
            case LE:
                return "<=";
        }
        return null;
    }

    /**
     * Operador: AND
     */
    public static final int AND = 1;

    /**
     * Operador: OR
     */
    public static final int OR = 2;

    /**
     * Operador: PROD
     */
    public static final int PROD = 3;

    /**
     * Operador: DIV
     */
    public static final int DIV = 4;

    /**
     * Operador: MOD
     */
    public static final int MOD = 5;

    /**
     * Operador: PLUS
     */
    public static final int PLUS = 6;

    /**
     * Operador: MINUS
     */
    public static final int MINUS = 7;

    /**
     * Operador: EQ
     */
    public static final int EQ = 8;

    /**
     * Operador: NEQ
     */
    public static final int NEQ = 9;

    /**
     * Operador: GT
     */
    public static final int GT = 10;

    /**
     * Operador: GE
     */
    public static final int GE = 11;

    /**
     * Operador: LT
     */
    public static final int LT = 12;

    /**
     * Operador: LE
     */
    public static final int LE = 13;
}
