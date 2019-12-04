package beestbot.mode.AbsurdMode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import beestbot.io.FileSerialization;
import beestbot.util.Configuration;

@TeleOp(name = "ScriptAbsurdMode", group = "OpMode")
@Disabled
public class ScriptAbsurdMode extends OpMode {

    public void description() {
        Configuration.debugMessage = Configuration.debugMessage + "WARNING: No script implemented!";
    }

    @Override
    public void init() {
        super.msStuckDetectInit = 5000;
        super.msStuckDetectInitLoop = 5000;
        super.msStuckDetectStart = 5000;
        super.msStuckDetectLoop = 5000;
        super.msStuckDetectStop = 10000;

        description();

        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        long lStartTime = System.currentTimeMillis();

        long timeElapsed = System.currentTimeMillis() - lStartTime;
        telemetry.addData("Record", "timeElapsed = %d", timeElapsed);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void start() {
        Configuration.visionManager.disable();
        resetStartTime();

        // ...

        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();

        // stop after success
        requestOpModeStop();
    }

    @Override
    public void loop() {
        // ...

        Configuration.debugMessage = Configuration.debugMessage + "ERROR: Script runs unsuccessfully!";

        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void stop() {
        // ...
        telemetry.addData("Time", "time = %f", time);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }
}