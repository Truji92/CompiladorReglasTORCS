package ast.structs;

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
                        "public class "+ extractName(Filename)+" extends Controller {\n" +
                        "\n" +
                        genJavaCodeDeclarations() + "\n" +
                        rules.genJavaCode()+"\n"+
                        "}";
        return javaCode;
    }

    private String extractName(String filename) {
        String auxFilename = filename;
        while(!auxFilename.matches("[^/]*.tc")){
            int index = auxFilename.indexOf('/') + 1;
            auxFilename = auxFilename.substring(index);
        }
        return auxFilename.substring(0, auxFilename.length()-3);
    }

    private String genJavaCodeDeclarations() {
        String javaCode = "";
        for (Declaration declaration: declarations){
            javaCode +=
                    "\n" +
                    "\t"+declaration.genJavaCode() +
                    "\n";
        }
        return javaCode;
    }
}
