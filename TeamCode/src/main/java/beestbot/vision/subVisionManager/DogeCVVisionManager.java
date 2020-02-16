package beestbot.vision.subVisionManager;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.opencv.core.Rect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Locale;

import beestbot.state.SensorSignals;
import beestbot.vision.sample.StoneDetector;

public class DogeCVVisionManager {

    private static final String WEBCAME_NAME = "Webcam 1";

    private OpenCvCamera webcam;
    private StoneDetector stoneDetector;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public DogeCVVisionManager(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, WEBCAME_NAME), cameraMonitorViewId);
    }

    // call from SkyStone Manager
    public void init() {
        webcam.openCameraDevice();
        stoneDetector = new StoneDetector();
        webcam.setPipeline(stoneDetector);
        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
    }

    // call from SkyStone Manager
    public void init_loop() {

    }

    // call from SkyStone Manager
    public void start() {

    }

    // call from SkyStone Manager
    public VectorF loop() {
        VectorF v = null;

        if(stoneDetector.isDetected()){
            try {
                for (int i = 0; i < stoneDetector.foundRectangles().size(); i++) {
                    Rect rectangle = stoneDetector.foundRectangles().get(i);
                    telemetry.addData("Stone X: " + (i + 1), rectangle.x);
                    telemetry.addData("Stone Y: " + (i + 1), rectangle.y);

                    v = new VectorF((float)rectangle.x, (float)rectangle.y);
                }
            }catch (Exception e){
                telemetry.addData("Stones", "Not Detected");
            }
        }

        telemetry.addData("Frame Count", webcam.getFrameCount());
        telemetry.addData("FPS", String.format(Locale.US, "%.2f", webcam.getFps()));
        telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
        telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
        telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
        telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
        telemetry.update();

        return v;
    }

    // call from SkyStone Manager
    public void stop() {
        webcam.stopStreaming();
    }


    public SensorSignals fetch() {
        return SensorSignals.UNKNOWN;
    }
}
