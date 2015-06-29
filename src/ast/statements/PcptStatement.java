package ast.statements;

/**
 * Created by alejandro on 28/06/15.
 */
public abstract class PcptStatement extends Statement{
    public PcptStatement(int type) {
        super(type);
    }
    public abstract boolean returns();
}
