package ast.statements;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class PcptBlockStm extends PcptStatement {

    private List<PcptStatement> statements;

    public PcptBlockStm(List<PcptStatement> statements) {
        super(VOID_TYPE); //TODO
        this.statements = statements;
    }

    public PcptBlockStm() {
        super(VOID_TYPE);
    }

    public void addStatement(PcptStatement statement) {
        statements.add(statement);
    }
}
