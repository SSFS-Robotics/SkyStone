package beestbot.motion;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import beestbot.io.GamepadManager;
import beestbot.state.Node;

public class NodeManager {

    public Node currentNode;
    public Node nextNode;

    public NodeManager(Node currentNode) {
        this.currentNode = currentNode;

    }

    public GamepadManager getAction(Node from, Node to) {
        GamepadManager gamepadManager = new GamepadManager();

        // get action
        gamepadManager.forceArmLeftMotor = 0; // like this

        return gamepadManager;
    }

}
