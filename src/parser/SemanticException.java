package parser;

/**
 * Created by alejandro on 28/06/15.
 */
public class SemanticException extends Exception {


    public static final int UNDEFINED = 0;
    public static final int VAR_NOT_DECLARED = 1;
    public static final int NO_EXISTE_ACCION = 2;
    public static final int TYPE_MISSMATCH = 3;
    public static final int DECLARACION_DOBLE = 4;
    public static final int NO_EXISTE_PERCEPCION = 5;
    public static final int WRONG_SIGNATURE = 6;
    public static final int PERCEPTION_DOESNT_RETURN =7;
    public static final int ONLY_READ_VAR = 8;



    private Token token;

    private int type;

    private final String msg;
    private String name = "";

    public SemanticException(int type, Token token) {
        this.type = type;
        this.token = token;
        msg = "Error en linea: " + token.getRow()+". "+getMsgForType();
    }

    public SemanticException(int type, Token token, String name) {
        this.type = type;
        this.token = token;
        this.name = name;
        msg = "Error en linea: " + token.getRow()+". "+getMsgForType();
    }

    private String getMsgForType() {
        switch (type) {
            case VAR_NOT_DECLARED:
                return "Variable no declarada.";
            case NO_EXISTE_ACCION:
                return "No existe una acción con ese nombre.";
            case NO_EXISTE_PERCEPCION:
                return "No existe una percepción con ese nombre.";
            case WRONG_SIGNATURE:
                return "No existe una llamada a esa función con esos parámetros.";
            case TYPE_MISSMATCH:
                return "Tipos no compatibles.";
            case DECLARACION_DOBLE:
                return name + " Ya existe una entidad con ese nombre.";
            case PERCEPTION_DOESNT_RETURN:
                return "perception "+name+" no siempre devuelve un valor. Las percepciones deben devolver siempre un valor boolean(true o false).";
            case ONLY_READ_VAR:
                return "El contenido de la variable "+name+" no puede modificarse.";
            default:
                return "Error semantico.";
        }
    }

    public SemanticException(){msg = "balbalba";}

    @Override
    public String toString() {
        return msg + "\n\n\n";
    }
}
