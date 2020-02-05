package beestbot.mode.AbsurdMode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import beestbot.io.GamepadManager;
import beestbot.motion.MotionManager;
import beestbot.util.Configuration;

@TeleOp(name = "BeestAbsurdMode", group = "OpMode")
@Disabled

// TODO: WARNING - When there is an issue updating the opMode, try: Build->Clean Project
public class BeestAbsurdMode extends OpMode {
    /*
    Description: This is a base mode for any OpMode in the design.
    All other OpMode should inherit this mode.
     */

    // Declare OpMode members.
    // 1000ticks = 1sec

    // variables in the father class
    // time = 0.0;
    // gamepad1;
    // gamepad2;
    // telemetry;
    // hardwareMap;
    // startTime;

    // functions in the father class
    // OpMode()
    // init() This method will be called once when the INIT button is pressed.
    // init_loop() This method will be called repeatedly once when the INIT button is pressed.
    // enable() This method will be called once when this op mode is running.
    // loop() This method will be called repeatedly in a loop while this op mode is running.
    // stop() This method will be called when this op mode is first disabled
    // requestOpModeStop() Shutdown the current OpMode
    // resetStartTime() set time to 0
    // internalPreInit(); internalPostInitLoop(); internalPostLoop();

    public MotionManager motionManager;

    public void setTeam() {
        throw new UnsupportedOperationException("setTeam() method not implemented!");
    }
    public void setSide() {
        throw new UnsupportedOperationException("setTemprename() method not implemented!");
    }
    public void setState() {
        throw new UnsupportedOperationException("setState() method not implemented!");
    }
    public void setVisionManager() {
        throw new UnsupportedOperationException("setVisionManager() method not implemented!");
    }


    public void sub_init() {
        throw new UnsupportedOperationException("sub_init() method not implemented");
    }

    public void sub_init_loop() {
        throw new UnsupportedOperationException("sub_init_loop() method not implemented");
    }

    public void sub_start() {
        throw new UnsupportedOperationException("sub_start() method not implemented");
    }

    public void sub_loop() {
        throw new UnsupportedOperationException("sub_loop() method not implemented");
    }

    public void sub_stop() {
        throw new UnsupportedOperationException("sub_stop() method not implemented");
    }

    @Override
    public void init() {
        // CAUTION: Configuration must be re-initialized
        // otherwise, memory will remain in the app when
        // switching to different modes or re-run the program
        Configuration.init();

        super.msStuckDetectInit     = 5000;
        super.msStuckDetectInitLoop = 5000;
        super.msStuckDetectStart    = 5000;
        super.msStuckDetectLoop     = 5000;
        super.msStuckDetectStop     = 10000;

        // initiate modes based on specific settings
        setTeam();
        setSide();
        setState();
        setVisionManager();

        // 3 managers get initiated
        motionManager = new MotionManager(telemetry, hardwareMap);
        Configuration.gamepadManager = new GamepadManager();

        // vision manager
        Configuration.visionManager.init();

        // TODO
        sub_init();

        // debug
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void init_loop() {
        // debug
        long lStartTime = System.currentTimeMillis();

        // vision manager
        Configuration.visionManager.init_loop();

        sub_init_loop();

        // TODO
        // debug
        long timeElapsed = System.currentTimeMillis() - lStartTime;
        telemetry.addData("DEBUG", "timeElapsed = %d", timeElapsed);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void start() {
        // debug
        resetStartTime();

        // vision manager
        Configuration.visionManager.start();

        // TODO
        sub_start();

        // debug
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void loop() {
        // vision manager
        Configuration.visionManager.loop();

        // TODO
        sub_loop();

        // debug
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }

    @Override
    public void stop() {
        // vision manager
        Configuration.visionManager.stop();

        // TODO
        sub_stop();

        // debug
        telemetry.addData("Time", "time = %f", time);
        telemetry.addData("DEBUG", Configuration.debugMessage);
        telemetry.update();
    }
}