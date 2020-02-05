package beestbot.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import beestbot.io.GamepadManager;
import beestbot.state.Inverse;
import beestbot.state.SensorSignals;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Task;
import beestbot.state.Team;
import beestbot.vision.MineralMasterVision;
import beestbot.vision.VisionManager;

public class Configuration {
    /*
        String Initialization
     */
    public static final String FRONT_LEFT_MOTOR = "dm1";
    public static final String FRONT_RIGHT_MOTOR = "dm0";
    public static final String BACK_LEFT_MOTOR = "dm2";
    public static final String BACK_RIGHT_MOTOR = "dm3";
    public static final String ARM_LEFT_MOTOR = "";
    public static final String ARM_RIGHT_MOTOR = "";
    public static final String LIFT_MOTOR = "um1";

    public static final String FRONT_LEFT_SERVO = "ds0";
    public static final String FRONT_RIGHT_SERVO = "ds1";
    public static final String CLIP_SERVO = "ds2"; //0.7 -> 1
    public static final String TOUCH_SERVO = "";

    public static final String LEFT_DISTANCE_SENSOR = "";
    public static final String RIGHT_DISTANCE_SENSOR = "";
    public static final String TOUCH_SENSOR = "";
    public static final String COLOR_SENSOR = "";

    /*
        STATE MACHINE
     */
    private static Team team;
    public static Inverse inverse = Inverse.X_AXIS;
    private static State state;
    private static Side side;
    private static SensorSignals sensorSignal = SensorSignals.UNKNOWN;

    public static Stack<Task> tasks = new Stack<>();
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
    public static ArrayList<GamepadManager> inversedGamepadsTimeStream = new ArrayList<>();
    public static VisionManager visionManager;
    public static GamepadManager gamepadManager;
    public static String debugMessage = "";

    public static void init() {
        team = null;
        inverse = Inverse.X_AXIS;
        state = null;
        side = null;
        sensorSignal = SensorSignals.UNKNOWN;
        tasks = new Stack<>();
        signals = new LinkedList<>();
        signal = null;
        currentTask = null;
        gamepadsTimeStream = new ArrayList<>();
        inversedGamepadsTimeStream = new ArrayList<>();
        visionManager = null;
        gamepadManager = null;
        debugMessage = null;
    }

    public static String getFileName() {
        assert team != null;
        assert side != null;
        assert sensorSignal != null;
        return team.toString() + "-" + side.toString() + "-" + sensorSignal.toString() + "_LOG.txt";
    }

    public static String getInverseFileName() {
        assert team != null;
        assert side != null;
        assert sensorSignal != null;
        if (team == Team.BLUE) {
            return Team.RED + "-" + side + "-" + sensorSignal.toString() + "_LOG.txt";
        } else if (team == Team.RED) {
            return Team.BLUE + "-" + side + "-" + sensorSignal.toString() + "_LOG.txt";
        } else {
            throw new UnsupportedOperationException("getInverseFileName() method has not implemented Team!");
        }
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
