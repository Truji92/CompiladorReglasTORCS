package ast.structs;

import java.io.File;
import java.net.FileNameMap;
import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Controller {
    // TODO

    private final List<Declaration> declarations;
    private final Rules rules;


    public Controller(List<Declaration> declarations, Rules rules) {
        this.declarations = declarations;
        this.rules = rules;
    }


    public String genJavaCode(String Filename) {
        String javaCode =
                "import champ2011client.*;\n" +
                        "\n" +
                        "public class "+ Filename+" extends Controller {\n" +
                        "\n" +
                        genJavaCodeDeclarations() + "\n" +
                        rules.genJavaCode()+"\n"+
                        "}";
        return javaCode;
    }

    private String genJavaCodeDeclarations() {
        String javaCode = "";
        for (Declaration declaration: declarations){
            javaCode +=
                    "\n" +
                    declaration.genJavaCode() +
                    "\n";
        }
        return javaCode;
    }
}
