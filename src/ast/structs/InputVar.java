package ast.structs;

/**
 * Created by alejandro on 28/06/15.
 */
public class InputVar extends Variable {


    public static final int SENSOR_0 = 0;
    public static final int SENSOR_1 = 1;
    public static final int SENSOR_2 = 2;
    public static final int SENSOR_3 = 3;
    public static final int SENSOR_4 = 4;
    public static final int SENSOR_5 = 5;
    public static final int SENSOR_6 = 6;
    public static final int SENSOR_7 = 7;
    public static final int SENSOR_8 = 8;
    public static final int SENSOR_9 = 9;
    public static final int SENSOR_10 = 10;
    public static final int SENSOR_11 = 11;
    public static final int SENSOR_12 = 12;
    public static final int SENSOR_13 = 13;
    public static final int SENSOR_14 = 14;
    public static final int SENSOR_15 = 15;
    public static final int SENSOR_16 = 16;
    public static final int SENSOR_17 = 17;
    public static final int SENSOR_18 = 18;
    public static final int GEAR = 19;
    public static final int SPEED = 20;
    public static final int ANGLE = 21;
    public static final int POSITION = 22;
    public static final int RPM = 23;


    private final int id;

    public InputVar(String name) {
        super(name, DOUBLE_TYPE, true);
        this.id = getKind(name);
        if (id == GEAR)
            super.setType(INT_TYPE);
    }

    @Override
    public String genJavaCodeRef() {
        switch (id) {
           case GEAR:
               return "sensors.getGear()";
           case SPEED:
               return "sensors.getSpeed()";
           case ANGLE:
               return "sensors.getAngleToTrackAxis()";
           case POSITION:
               return "sensors.getTrackPosition()";
           case RPM:
               return "sensors.getRPM()";
           default:
               return "sensors.getTrackEdgeSensors()["+id+"]";
        }
    }

    private int getKind(String lexeme) {
        if (lexeme.equals("speed")) return SPEED;
        else if (lexeme.equals("angle")) return ANGLE;
        else if (lexeme.equals("position")) return POSITION;
        else if (lexeme.equals("rpm")) return RPM;
        else if (lexeme.equals("sensor0")) return SENSOR_0;
        else if (lexeme.equals("sensor1")) return SENSOR_1;
        else if (lexeme.equals("sensor2")) return SENSOR_2;
        else if (lexeme.equals("sensor3")) return SENSOR_3;
        else if (lexeme.equals("sensor4")) return SENSOR_4;
        else if (lexeme.equals("sensor5")) return SENSOR_5;
        else if (lexeme.equals("sensor6")) return SENSOR_6;
        else if (lexeme.equals("sensor7")) return SENSOR_7;
        else if (lexeme.equals("sensor8")) return SENSOR_8;
        else if (lexeme.equals("sensor9")) return SENSOR_9;
        else if (lexeme.equals("sensor10")) return SENSOR_10;
        else if (lexeme.equals("sensor11")) return SENSOR_11;
        else if (lexeme.equals("sensor12")) return SENSOR_12;
        else if (lexeme.equals("sensor13")) return SENSOR_13;
        else if (lexeme.equals("sensor14")) return SENSOR_14;
        else if (lexeme.equals("sensor15")) return SENSOR_15;
        else if (lexeme.equals("sensor16")) return SENSOR_16;
        else if (lexeme.equals("sensor17")) return SENSOR_17;
        else if (lexeme.equals("sensor18")) return SENSOR_18;
        else if (lexeme.equals("gear")) return GEAR;
        else return -1;
    }
}
