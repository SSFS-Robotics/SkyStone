package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import beestbot.state.SensorSignals;
import beestbot.vision.subVisionManager.TensorflowVisionManager;
import beestbot.vision.subVisionManager.VuforiaVisionManager;
import kotlin.NotImplementedError;

public class SkyStoneVsionManager extends NullVsionManager {
    private static final boolean USE_FLASH = false;
    private static final boolean USE_VUFORIA = true;
    private static final boolean USE_TENSORFLOW = false;
    private static final boolean USE_OPENCV = false;

    private VuforiaVisionManager vuforiaVisionManager;
    private TensorflowVisionManager tensorflowVisionManager;

    public SkyStoneVsionManager(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);

        if (USE_VUFORIA || USE_TENSORFLOW) {
            vuforiaVisionManager = new VuforiaVisionManager(hardwareMap, telemetry);
        }

        if (USE_TENSORFLOW) {
            assert vuforiaVisionManager != null;
            tensorflowVisionManager = new TensorflowVisionManager(vuforiaVisionManager.getVuforiaLocalizer(), hardwareMap);
        }
    }

    public VuforiaVisionManager getVuforiaVisionManager() {
        return vuforiaVisionManager;
    }

    public TensorflowVisionManager getTensorflowVisionManager() {
        return tensorflowVisionManager;
    }

    @Override
    public void enable() {
        CameraDevice.getInstance().setFlashTorchMode(USE_FLASH);
    }

    @Override
    public void end() {
        CameraDevice.getInstance().setFlashTorchMode(false);
    }

    @Override
    public void disable() {
        CameraDevice.getInstance().setFlashTorchMode(false);
    }

    // call from main program
    // call to sub vision manager
    public void init() {
        if (USE_VUFORIA) {
            vuforiaVisionManager.init();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.init();
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
    }

    // call from main program
    // call to sub vision manager
    public void stop() {
        if (USE_VUFORIA) {
            vuforiaVisionManager.stop();
        }
        if (USE_TENSORFLOW) {
            tensorflowVisionManager.stop();
        }
    }

    public SensorSignals fetch() {
        if (USE_VUFORIA) {
            return vuforiaVisionManager.fetch();
        }
        if (USE_TENSORFLOW) {
            return tensorflowVisionManager.fetch();
        }
        throw new NotImplementedError("Cannot fetch Sensor Signals because no method (Tensorflow, Vuforia, or OpenCV) is not implemented.");
    }

}

