package ast.statements;

import ast.Type;

/**
 * Created by alejandro on 28/06/15.
 */
public abstract class Statement implements Type {
    private int type;

    public Statement(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
