package beestbot.mode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import beestbot.mode.AbsurdMode.HankesAbsurdIntelligence;
import beestbot.util.Configuration;
import beestbot.state.Side;
import beestbot.state.State;
import beestbot.state.Team;

@Autonomous(name = "WallfacerKokiAutoNext", group = "Autonomous")
public class WallfacerKokiAutoNext extends HankesAbsurdIntelligence {

    @Override
    public void setTeam() {
        Configuration.setTeam(Team.BLUE);
    }

    @Override
    public void setSide() {
        Configuration.setSide(Side.Quarry);
    }

    @Override
    public void setState() {
        Configuration.setState(State.AUTONOMOUS);
    }
}
