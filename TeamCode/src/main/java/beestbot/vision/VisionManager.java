package beestbot.vision;

import beestbot.state.SensorSignals;

public interface VisionManager {
    MineralMasterVision vision = null;

    void enable();

    SensorSignals fetch();

    void disable();
}

