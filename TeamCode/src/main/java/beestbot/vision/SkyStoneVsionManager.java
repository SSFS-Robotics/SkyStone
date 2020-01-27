package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import beestbot.state.SensorSignals;

public class SkyStoneVsionManager extends NullVsionManager {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    private static final String WEBCAME_NAME = "";
    private static final boolean VUFORIA_SCREEN = false;
    private static final boolean USE_FLASH = false;

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public SkyStoneVsionManager(HardwareMap hardwareMap) {
        super(hardwareMap);

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
        if (WEBCAME_NAME != "") {parameters.cameraName = hardwareMap.get(WebcamName.class, WEBCAME_NAME);}
        parameters.vuforiaLicenseKey = "AfdTR9D/////AAABmTD0Gry/NUengHVpoCy0wm4Eqk0VfLshQ6EDUICCgGa8JSEvUtzK5zfxO8yOev9NDT4epFyH22QcPD/fJPcEhs9eRFp0DCU6R9RCgrjYNwqbjKnwyj77nWs8lIBM3W3UZE1accXjDeEUcPBKIlV0+0ALLJwJPPmOsTidwnX1UWLUQgl4wq1wMjSRryKg6z7gJtfv7QPRzg2g9V3/oDfirNzso2jB9APcr86oCXCjNcCcWE+i/jNUhG9XgnMKpUARuSaS3aDXo/3vhm5SkAuDSxgqa/rjT8HGLPjDqFd8uySA3zNMgSzaK0161VqcC6DNA5Jbs9VmO8/G7ZHGItVp0xMcmyGZzw/IBeOhK/LySUd4";
//        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

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
        CameraDevice.getInstance().setFlashTorchMode(USE_FLASH);
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

