package beestbot.mode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import beestbot.state.Facing;
import beestbot.state.State;
import beestbot.state.Team;
import beestbot.util.Configuration;

@TeleOp(name = "FlintMode", group = "TeleOp")
public class FlintMode extends AbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    } // TODO: subsitude this with a new value

    @Override
    public void setFacing() {
        Configuration.setFacing(Facing.WALLBREAKER);
    } // TODO: subsitude this with a new value

    @Override
    public void setState() {
        Configuration.setState(State.CONTROL);
    }
}
