package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import beestbot.state.SensorSignals;

public class SkyStoneVsionManager extends NullVsionManager {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private boolean useFlash;

    private boolean VUFORIA_SCREEN = false;

    public SkyStoneVsionManager(HardwareMap hardwareMap, MineralMasterVision.TFLiteAlgorithm infer, boolean useFlash) {
        super(hardwareMap);
        this.useFlash = useFlash;

        setupVuforia(hardwareMap);
        setupTensorflow(hardwareMap);
    }

    private void setupVuforia(HardwareMap hardwareMap) {
        // Initiate vuforia
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // if you want to show the screen, put cameraMonitorViewId in it; else leave it empty
        VuforiaLocalizer.Parameters parameters;
        if (VUFORIA_SCREEN) {
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        }
        else {
            parameters = new VuforiaLocalizer.Parameters();
        }
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.vuforiaLicenseKey = "AYVp8HD/////AAAAmbWvbJCPAUEivTbJDYmkKlUhuPRlEN5MxRGtGpK68YAYgdTUSycNhLm/AQ2nxYbFwiX+eiXtdzMQg/h0/OO0uHdiq2AGB9qus774oqnqQ2DrzfdUARClxtcnFwJw3Ba/tyvP/gxWjMWetKcwfdDAjD+dilVMrqS7ePsZZPzjSaNB/kjaP3yQRTN1D/050KdnxwKicMkqhulqKv1miESfNBm7qQd3h9FZJoVZumqfytS7pMmqAjvSN7TGcQw7vxw7DJAECvRfoFhuszWNjwcF3rwRsQEXr1jynbJvhh8z4SJdJDqIK4EEroLLSpHVTYj9si4xULph02bAc2fUXDPMS/g7VfFZcgKuzFvZ/eR3ZHCm";

        // Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // just to be consistent with OpenCV pipeline
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
    }

    private void setupTensorflow(HardwareMap hardwareMap) {
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()){ throw new IllegalStateException("This device is not compatible with TFOD; FUCK");}

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void enable() {
        tfod.activate();
        CameraDevice.getInstance().setFlashTorchMode(useFlash);
    }

    public void end() {
        tfod.deactivate();
        CameraDevice.getInstance().setFlashTorchMode(false);
    }

    public SensorSignals fetch() {
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            float maxConfidence = 0;
            Recognition bestRecognition = null;
            for (Recognition recognition : updatedRecognitions) {

//                float width = 20.0f;
//                float height = 12.5f;
//                String label = recognition.getLabel();
                float confidence = recognition.getConfidence();
//                float left = recognition.getLeft();
//                float top = recognition.getTop();
//                float right = recognition.getRight();
//                float botton = recognition.getBottom();

                if (confidence > maxConfidence) {
                    maxConfidence = confidence;
                    bestRecognition = recognition;
                }
            }
            if (bestRecognition == null) {
                return SensorSignals.NOTHING;
            } else if (bestRecognition.equals(LABEL_FIRST_ELEMENT)) {
                return SensorSignals.STONE;
            } else if (bestRecognition.equals(LABEL_SECOND_ELEMENT)) {
                return SensorSignals.SKYSTONE;
            } else {
                return SensorSignals.UNKNOWN;
            }
        }
        return SensorSignals.UNKNOWN;
    }

    public void disable() {
        tfod.shutdown();
        CameraDevice.getInstance().setFlashTorchMode(false);
    }
}

