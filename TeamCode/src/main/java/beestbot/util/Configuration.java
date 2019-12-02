package beestbot.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import beestbot.io.GamepadManager;
import beestbot.state.Side;
import beestbot.state.SensorSignals;
import beestbot.state.State;
import beestbot.state.Task;
import beestbot.state.Team;
import beestbot.vision.MineralMasterVision;
import beestbot.vision.SkyStoneVsionManager;

public class Configuration {
    /*
        String Initialization
     */
    public static final String FRONT_LEFT_MOTOR = "dm1";
    public static final String FRONT_RIGHT_MOTOR = "dm0";
    public static final String BACK_LEFT_MOTOR = "um3";
    public static final String BACK_RIGHT_MOTOR = "dm3";

    public static final String ARM_LEFT_MOTOR = "";
    public static final String ARM_RIGHT_MOTOR = "";
    public static final String LIFT_MOTOR = "um1";

    public static final String FRONT_LEFT_SERVO = "us0";
    public static final String FRONT_RIGHT_SERVO = "us1";
    public static final String CLIP_SERVO = "us2";
    public static final String TOUCH_SERVO = "";

    public static final String LEFT_DISTANCE_SENSOR = "";
    public static final String RIGHT_DISTANCE_SENSOR = "";
    public static final String TOUCH_SENSOR = "";
    public static final String COLOR_SENSOR = "";

    /*
        STATE MACHINE
     */
    private static Team team;
    private static State state;
    private static Side side;
    private static SensorSignals sensorSignal = SensorSignals.UNKNOWN;

    public static Queue<Task> tasks = new LinkedList<>();
    public static Queue<SensorSignals> signals = new LinkedList<>();
    public static SensorSignals signal;
    public static Task currentTask;

    /*
        Settings
     */
    public static final MineralMasterVision.TFLiteAlgorithm INFER = MineralMasterVision.TFLiteAlgorithm.INFER_RIGHT;
    public static final Integer maximumStream = 5;

    public static final boolean ENCODER = true; //TODO update encoder
    public static final boolean flashLight = false;
    public static final Integer TETRIX_TICKS_PER_REV = 1440;
    public static final Integer ANDYMARK_TICKS_PER_REV = 1120;
    public static final float ABSOLUTE_SPEED = 1.0f;

    /*
        Variables
     */
    public static ArrayList<GamepadManager> gamepadsTimeStream = new ArrayList<>();
    public static SkyStoneVsionManager visionManager;
    public static GamepadManager gamepadManager;
    public static String debugMessage = "";

    public static String getFileName() {
        assert team != null;
        assert side != null;
        assert sensorSignal != null;
        return team.toString() + "-" + side.toString() + "-" + sensorSignal + "_LOG.txt";
    }

    public static State getState() {
        return state;
    }
    public static void setState(State state) {
        Configuration.state = state;
    }

    public static Team getTeam() {
        return team;
    }
    public static void setTeam(Team team) {
        Configuration.team = team;
    }
    public static Side getSide() {
        return side;
    }
    public static void setSide(Side side) {
        Configuration.side = side;
    }
    public static SensorSignals getSensorSignal() {
        return sensorSignal;
    }
    public static void setSensorSignal(SensorSignals sensorSignal) {
        Configuration.sensorSignal = sensorSignal;
    }
}
