package beestbot.vision;

import beestbot.state.SensorSignals;

public interface VisionManager {
    MineralMasterVision vision = null;

    void enable();

    SensorSignals fetch();

    void disable();

    void init();
    void init_loop();
    void start();
    void loop();
    void stop();
}

