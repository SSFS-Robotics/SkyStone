package beestbot.mode.Recording;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;
import beestbot.util.Configuration;
import beestbot.vision.NullVsionManager;

@Autonomous(name = "RecordBlueQuarryWithoutVision", group = "Record")
public class RecordBlueQuarryWithoutVision extends HankesAbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    }

    @Override
    public void setSide() {
        Configuration.setSide(Side.QUARRY);
    }

    @Override
    public void setState() {
        Configuration.setState(State.RECORDING);
    }

    @Override
    public void setVisionManager() {
        Configuration.visionManager = new NullVsionManager(hardwareMap, telemetry);
    }
}
