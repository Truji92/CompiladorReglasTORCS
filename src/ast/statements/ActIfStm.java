package ast.statements;

import ast.expression.Expression;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActIfStm extends ActStatement {
    private final Expression condition;

    private final ActStatement thenStm;
    private final ActStatement elseStm;

    public ActIfStm(Expression condition, ActStatement thenStm, ActStatement elseStm) {
        super(VOID_TYPE);
        this.condition = condition;
        this.thenStm = thenStm;
        this.elseStm = elseStm;
    }

    public Expression getCondition() {
        return condition;
    }

    public ActStatement getThenStm() {
        return thenStm;
    }

    public ActStatement getElseStm() {
        return elseStm;
    }

    @Override
    public String genJavaCode() {
        String javaCode = "if(" +condition.genJavaCode()+") " + thenStm.genJavaCode();
        if (elseStm != null) {
            javaCode += "\nelse " + elseStm.genJavaCode();
        }
        return javaCode;
    }
}
