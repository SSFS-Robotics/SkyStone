package beestbot.mode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.util.Configuration;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;

@Autonomous(name = "WallbreakerKokiAutoNext", group = "Autonomous")
public class WallbreakerKokiAutoNext extends HankesAbsurdIntelligence {

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
}
