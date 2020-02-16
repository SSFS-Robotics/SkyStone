package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import beestbot.state.SensorSignals;
import beestbot.vision.subVisionManager.DogeCVVisionManager;
import beestbot.vision.subVisionManager.TensorflowVisionManager;
import beestbot.vision.subVisionManager.VuforiaVisionManager;
import kotlin.NotImplementedError;

public class SkyStoneVsionManager extends NullVsionManager {
    private final boolean USE_FLASH = false;
    private boolean USE_VUFORIA;
    private boolean USE_TENSORFLOW;
    private boolean USE_OPENCV;

    private VuforiaVisionManager vuforiaVisionManager;
    private TensorflowVisionManager tensorflowVisionManager;
    private DogeCVVisionManager dogeCVVisionManager;

    public SkyStoneVsionManager(HardwareMap hardwareMap, Telemetry telemetry, boolean USE_VUFORIA, boolean USE_TENSORFLOW, boolean USE_OPENCV) {
        super(hardwareMap, telemetry);

        this.USE_VUFORIA = USE_VUFORIA;
        this.USE_TENSORFLOW = USE_TENSORFLOW;
        this.USE_OPENCV = USE_OPENCV;

        if (USE_TENSORFLOW && USE_VUFORIA) {
            vuforiaVisionManager = new VuforiaVisionManager(hardwareMap, telemetry);
            tensorflowVisionManager = new TensorflowVisionManager(vuforiaVisionManager.getVuforiaLocalizer(), hardwareMap);
        } else if (USE_VUFORIA) {
            vuforiaVisionManager = new VuforiaVisionManager(hardwareMap, telemetry);
        } else if (USE_OPENCV) {
            dogeCVVisionManager = new DogeCVVisionManager(hardwareMap, telemetry);
        } else {
            throw new IllegalArgumentException("I don't fucking know which Computer Vision system to use!");
        }
    }

    public VuforiaVisionManager getVuforiaVisionManager() {
        return vuforiaVisionManager;
    }

    public TensorflowVisionManager getTensorflowVisionManager() {
        return tensorflowVisionManager;
    }

    public DogeCVVisionManager getDogeCVVisionManager() {
        return dogeCVVisionManager;
    }

    // call from main program
    // call to sub vision manager
    public void init() {
        CameraDevice.getInstance().setFlashTorchMode(USE_FLASH);
        if (USE_VUFORIA) {
            vuforiaVisionManager.init();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.init();
        }
        if (USE_OPENCV) {
            dogeCVVisionManager.init();
        }

    }

    // call from main program
    // call to sub vision manager
    public void init_loop() {
        if (USE_VUFORIA) {
            vuforiaVisionManager.init_loop();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.init_loop();
        }
        if (USE_OPENCV) {
            dogeCVVisionManager.init_loop();
        }

    }

    // call from main program
    // call to sub vision manager
    public void start() {
        if (USE_VUFORIA) {
            vuforiaVisionManager.start();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.start();
        }
        if (USE_OPENCV) {
            dogeCVVisionManager.start();
        }
    }

    // call from main program
    // call to sub vision manager
    public void loop(Telemetry telemetry) {
        if (USE_VUFORIA) {
            vuforiaVisionManager.loop();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.loop();
        }
        if (USE_OPENCV) {
            dogeCVVisionManager.loop();
        }
    }

    // call from main program
    // call to sub vision manager
    public void stop() {
        CameraDevice.getInstance().setFlashTorchMode(false);
        if (USE_VUFORIA) {
            vuforiaVisionManager.stop();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.stop();
        }
        if (USE_OPENCV) {
            dogeCVVisionManager.stop();
        }
    }

    public SensorSignals fetch() {
        if (USE_VUFORIA) {
            return vuforiaVisionManager.fetch();
        }
        if (USE_TENSORFLOW) {
            return tensorflowVisionManager.fetch();
        }
        if (USE_OPENCV) {
            return dogeCVVisionManager.fetch();
        }
        throw new NotImplementedError("Cannot fetch Sensor Signals because no method (Tensorflow, Vuforia, or OpenCV) is not implemented.");
    }
}

