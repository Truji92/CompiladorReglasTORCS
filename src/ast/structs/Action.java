package ast.structs;

import ast.expression.Expression;
import ast.statements.ActBlockStm;

/**
 * Created by alejandro on 28/06/15.
 */
public class Action extends Method {
    private int type = VOID_TYPE;
    private ActBlockStm body;

    public Action(String name, SymbolTable symboltable) {
        super(symboltable, name);

    }

    public ActBlockStm getBody() {
        return body;
    }

    public void setBody(ActBlockStm body) {
        this.body = body;
    }

    @Override
    public String genJavaCode() {
        String javaCode = "private void " + name + "(SensorModel sensors, Action action" + genArgumentsCode() +")\n";
        javaCode += body.genJavaCode();
        return javaCode;
    }



}
