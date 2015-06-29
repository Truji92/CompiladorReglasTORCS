package ast.structs;

/**
 * Created by alejandro on 28/06/15.
 */
public class InnerDecl extends Declaration {

    private final Variable var;

    public InnerDecl(Variable var) {
        this.var = var;
    }

    public Variable getVar() {
        return var;
    }
}
