package ast.structs;

/**
 * Created by alejandro on 28/06/15.
 */
public class OutputVar extends Variable {

    public static final int GEAR = 33;
    public static final int ACCELERATE = 34;
    public static final int BRAKE = 35;
    public static final int STEERING = 36;

    private final int id;

    public OutputVar(String name) {
        super(name, DOUBLE_TYPE, false);
        id = getKind(name);
        if (id == GEAR)
            super.setType(INT_TYPE);

    }

    private int getKind(String lexeme) {
        if (lexeme.equals("accelerate")) return ACCELERATE;
        else if (lexeme.equals("gear")) return GEAR;
        else if (lexeme.equals("brake")) return BRAKE;
        else if (lexeme.equals("steering")) return STEERING;
        else return -1;
    }

    @Override
    public String genJavaCodeRef() {
        return "action."+name;
    }
}