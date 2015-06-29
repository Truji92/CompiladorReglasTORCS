package ast.structs;

import java.util.List;

/**
 * Created by alejandro on 28/06/15.
 */
public class Controller {
    // TODO

    private List<Declaration> declarations;
    private Rules rules;


    public Controller(List<Declaration> declarations, Rules rules) {
        this.declarations = declarations;
        this.rules = rules;
    }
}
