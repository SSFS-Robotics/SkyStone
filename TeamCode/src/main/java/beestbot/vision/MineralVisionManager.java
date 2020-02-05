package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import beestbot.util.Configuration;
import beestbot.state.SensorSignals;

public class MineralVisionManager extends NullVsionManager {
    private MineralMasterVision vision;

    public MineralVisionManager(HardwareMap hardwareMap, Telemetry telemetry, MineralMasterVision.TFLiteAlgorithm infer, boolean useFlash) {
        super(hardwareMap, telemetry);
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.vuforiaLicenseKey = "AYVp8HD/////AAAAmbWvbJCPAUEivTbJDYmkKlUhuPRlEN5MxRGtGpK68YAYgdTUSycNhLm/AQ2nxYbFwiX+eiXtdzMQg/h0/OO0uHdiq2AGB9qus774oqnqQ2DrzfdUARClxtcnFwJw3Ba/tyvP/gxWjMWetKcwfdDAjD+dilVMrqS7ePsZZPzjSaNB/kjaP3yQRTN1D/050KdnxwKicMkqhulqKv1miESfNBm7qQd3h9FZJoVZumqfytS7pMmqAjvSN7TGcQw7vxw7DJAECvRfoFhuszWNjwcF3rwRsQEXr1jynbJvhh8z4SJdJDqIK4EEroLLSpHVTYj9si4xULph02bAc2fUXDPMS/g7VfFZcgKuzFvZ/eR3ZHCm";

        vision = new MineralMasterVision(parameters, hardwareMap, useFlash, infer);
        vision.init();
    }

    public void enable() {
        vision.enable();
    }

//    public void end() {
//        vision.disable();
//    }

    public SensorSignals fetch() {
        SensorSignals goldPosition = vision.getTfLite().getLastKnownSampleOrder();
        Configuration.setSensorSignal(goldPosition);
        return goldPosition;
    }

    public void disable() {
        vision.shutdown();
    }
}

