package beestbot.mode.Recording;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.util.Configuration;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;
import beestbot.vision.SkyStoneVsionManager;

@Autonomous(name = "RecordBlueFoundationWithVision", group = "Record")
public class RecordBlueFoundationWithVision extends HankesAbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    }

    @Override
    public void setSide() {
        Configuration.setSide(Side.FOUNDATION);
    }

    @Override
    public void setState() {
        Configuration.setState(State.RECORDING);
    }

    @Override
    public void setVisionManager() {
//        mineralVisionManager = new MineralVisionManager(hardwareMap, Configuration.INFER, Configuration.flashLight);
        Configuration.visionManager = new SkyStoneVsionManager(hardwareMap, telemetry, true, false, false);
    }
}
