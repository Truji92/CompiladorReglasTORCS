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

    @Override
    public boolean returns() {
        return statements.size() != 0 && statements.get(statements.size() - 1).returns();
    }

    @Override
    public String genJavaCode() {
        String javaCode = "{\n";
        for (PcptStatement statement: statements) {
            javaCode += statement.genJavaCode() + "\n";
        }
        javaCode += "}\n";
        return javaCode;
    }
}
