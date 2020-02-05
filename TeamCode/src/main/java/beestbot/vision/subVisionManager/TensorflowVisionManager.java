package beestbot.vision.subVisionManager;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import beestbot.state.SensorSignals;

public class TensorflowVisionManager {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private HardwareMap hardwareMap;

    public TensorflowVisionManager(VuforiaLocalizer vuforia, HardwareMap hardwareMap) {
        this.vuforia = vuforia;
        this.hardwareMap = hardwareMap;
    }

    // call from SkyStone Manager
    public void init() {
        if (!ClassFactory.getInstance().canCreateTFObjectDetector()){ throw new IllegalStateException("This device is not compatible with TFOD; FUCK");}

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    // call from SkyStone Manager
    public void init_loop() {

    }

    // call from SkyStone Manager
    public void start() {
        tfod.activate();
    }

    // call from SkyStone Manager
    public void loop() {
    }

    // call from SkyStone Manager
    public void stop() {
        tfod.deactivate();
        tfod.shutdown();
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

}
