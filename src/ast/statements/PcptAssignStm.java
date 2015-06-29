package ast.statements;

import ast.expression.Expression;
import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptAssignStm extends PcptStatement {

    private final Variable var;
    private final Expression assign;


    public PcptAssignStm(Variable var, Expression assign) {
        super(VOID_TYPE);
        this.var = var;
        this.assign = assign;
    }

    public Variable getVar() {
        return var;
    }

    public Expression getAssign() {
        return assign;
    }

    @Override
    public boolean returns() {
        return false;
    }

    @Override
    public String genJavaCode() {
        return var.name +" = "+assign.genJavaCode()+";";
    }
}

