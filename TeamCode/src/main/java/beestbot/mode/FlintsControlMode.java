package beestbot.mode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;
import beestbot.util.Configuration;
import beestbot.vision.NullVsionManager;
import beestbot.vision.SkyStoneVsionManager;

@TeleOp(name = "FlintsControlMode", group = "TeleOp")
public class FlintsControlMode extends HankesAbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    } // TODO: substitute this with a new value

    @Override
    public void setSide() {
        Configuration.setSide(Side.FOUNDATION);
    } // TODO: substitute this with a new value

    @Override
    public void setState() {
        Configuration.setState(State.CONTROL);
    }

    @Override
    public void setVisionManager() {
//        mineralVisionManager = new MineralVisionManager(hardwareMap, Configuration.INFER, Configuration.flashLight);
        Configuration.visionManager = new SkyStoneVsionManager(hardwareMap, telemetry);
    }
}
