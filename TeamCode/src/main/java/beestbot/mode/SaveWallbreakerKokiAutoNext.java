package beestbot.mode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.util.Configuration;
import beestbot.state.Facing;
import beestbot.state.State;
import beestbot.state.Team;

@Autonomous(name = "SaveWallbreakerKokiAutoNext", group = "Save")
public class SaveWallbreakerKokiAutoNext extends AbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    }

    @Override
    public void setFacing() {
        Configuration.setFacing(Facing.WALLBREAKER);
    }

    @Override
    public void setState() {
        Configuration.setState(State.RECORDING);
    }
}
