package ast.statements;

import ast.expression.BooleanLiteralExpression;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptReturnStm extends PcptStatement {

    private final BooleanLiteralExpression value;

    public PcptReturnStm(BooleanLiteralExpression value) {
        super(BOOLEAN_TYPE);
        this.value = value;
    }

    @Override
    public String genJavaCode() {
        return "return " + value.genJavaCode() + ";\n";
    }

    @Override
    public boolean returns() {
        return true;
    }
}
