package ast.statements;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class ActBlockStm extends ActStatement {

    private List<ActStatement> statements;

    public ActBlockStm(List<ActStatement> statements) {
        super(VOID_TYPE); //TODO
        this.statements = statements;
    }

    public ActBlockStm() {
        super(VOID_TYPE);
    }

    public void addStatement(ActStatement statement) {
        statements.add(statement);
    }

    @Override
    public String genJavaCode() {
        String javaCode = "{\n";
        for (ActStatement statement: statements) {
            javaCode += statement.genJavaCode() + "\n";
        }
        javaCode += "}\n";
        return javaCode;
    }
}
