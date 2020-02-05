package beestbot.mode.Scripts;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import beestbot.io.FileSerialization;
import beestbot.mode.AbsurdMode.ScriptAbsurdMode;
import beestbot.util.Configuration;

@TeleOp(name = "DeleteAllRecordScript", group = "OpMode")
public class DeleteAllRecordScript extends ScriptAbsurdMode {

    @Override
    public void description() {
        Configuration.debugMessage = Configuration.debugMessage + "WARNING: ALL RECORDING MODES WILL BE REMOVED!";
    }

    @Override
    public void start() {
        resetStartTime();

        // remove all records
        boolean success = FileSerialization.removeAllInternalRecordFiles(hardwareMap.appContext, telemetry);

        telemetry.addData("DEBUG", Configuration.debugMessage);
//        telemetry.update(); -> This is handeled in the main OpMode

        // stop after success
        if (success) {
            Configuration.debugMessage = Configuration.debugMessage + "DEBUG: Script runs successfully!";
            telemetry.addData("DEBUG", Configuration.debugMessage);
//            telemetry.update(); -> This is handeled in the main OpMode
            requestOpModeStop();
        }
    }
}