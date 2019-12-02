package beestbot.vision;

import beestbot.state.SensorSignals;

public abstract class VisionManager {
    private MineralMasterVision vision;

    public abstract void enable();

    public abstract SensorSignals fetch();

    public abstract void disable();
}

