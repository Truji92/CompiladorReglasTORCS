package ast.structs;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Controller {

    private final List<Declaration> declarations;
    private final Rules rules;


    public Controller(List<Declaration> declarations, Rules rules) {
        this.declarations = declarations;
        this.rules = rules;
    }


    public String genJavaCode(String Filename) {
        String name = extractName(Filename);
        String javaCode =
                "import champ2011client.*;\n" +
                        "\n" +
                        "public class "+ name+" extends Controller {\n" +
                        "\n" +
                        "\tpublic "+name+"() {\n" +
                        "\t\t System.out.println(\"Iniciando\");\n"+
                        "\t}\n\n"+
                        "\tpublic void reset() {\n"+
                        "\t\tSystem.out.println(\"Reiniciando la carrera\");\n"+
                        "\t}\n\n"+
                        "\tpublic void shutdown() { \n" +
                        "\t\tSystem.out.println(\"CARRERA TERMINADA\");\n" +
                        "\t}\n"+
                        genJavaCodeDeclarations() + "\n" +
                        rules.genJavaCode()+"\n"+
                        "}";
        return javaCode;
    }

    private String extractName(String filename) {
        int index1 = filename.lastIndexOf('/');
        int index2 = filename.lastIndexOf("\\");
        int index = Math.max(index1, index2);

        if (index == -1) return filename.substring(0, filename.length() - 3);
        return filename.substring(index+1, filename.length()-3);
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

    public String genJavaFilename(String filename) {
        return extractName(filename) + ".java";
    }
}
