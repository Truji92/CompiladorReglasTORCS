package ast.structs;

import ast.statements.PcptBlockStm;

/**
 * Created by alejandro on 28/06/15.
 */
public class Perception extends Method {

    private int type = BOOLEAN_TYPE;
    private PcptBlockStm body;

    public Perception(String name, SymbolTable symboltable) {
        super(symboltable, name);

    }

    public PcptBlockStm getBody() {
        return body;
    }

    public void setBody(PcptBlockStm body) {
        this.body = body;
    }

    @Override
    public String genJavaCode() {
        String javaCode = "private boolean "+name+"(SensorModel sensors"+genArgumentsCode()+")";
        javaCode += body.genJavaCode();
        return javaCode;
    }
}
