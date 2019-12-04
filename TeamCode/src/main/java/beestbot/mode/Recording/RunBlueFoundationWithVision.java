package beestbot.mode.Recording;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.util.Configuration;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;
import beestbot.vision.SkyStoneVsionManager;

@Autonomous(name = "RunBlueFoundationWithVision", group = "Autonomous")
public class RunBlueFoundationWithVision extends HankesAbsurdIntelligence {

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
        Configuration.setState(State.AUTONOMOUS);
    }

    @Override
    public void setVisionManager() {
//        mineralVisionManager = new MineralVisionManager(hardwareMap, Configuration.INFER, Configuration.flashLight);
        Configuration.visionManager = new SkyStoneVsionManager(hardwareMap, Configuration.INFER, Configuration.flashLight);
    }
}
