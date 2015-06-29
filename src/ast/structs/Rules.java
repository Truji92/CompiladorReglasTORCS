package ast.structs;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Rules {

    private final List<Rule> rules;

    public Rules(List<Rule> rules) {
        this.rules = rules;
    }

    public String genJavaCode() {
        String javaCode = "\tpublic Action control(SensorModel sensors) {\n" +
                          "\t\tAction action = new Action();";
        for (Rule rule: rules) {
            javaCode +=
                    "\n" +
                    "\t\t" + rule.genJavaCode();
        }
        javaCode += "\t\treturn action;\n" +
                    "\t}\n";
        return javaCode;

    }
}
