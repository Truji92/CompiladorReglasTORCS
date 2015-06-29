package ast.statements;

import ast.expression.Expression;
import ast.structs.Variable;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActDecl extends ActStatement{
    private final Variable var;
    private Expression assign;
    private final boolean declarationPlusAssign;

    public ActDecl(Variable var, Expression assign) {
        super(var.getType());
        this.var = var;
        this.assign = assign;
        declarationPlusAssign = true;
    }

    public ActDecl(Variable var) {
        super(var.getType());
        this.var = var;
        declarationPlusAssign = false;
    }

    @Override
    public String genJavaCode() {
        String javaCode = var.genJavaCode();
        if (declarationPlusAssign)
            javaCode += " = " + assign.genJavaCode();
        javaCode += ";";
        return javaCode;
    }
}
