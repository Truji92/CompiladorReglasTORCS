package ast.structs;

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

}
