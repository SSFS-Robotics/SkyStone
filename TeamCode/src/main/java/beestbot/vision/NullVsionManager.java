package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import beestbot.state.SensorSignals;

public class NullVsionManager implements VisionManager {

    private HardwareMap hardwareMap;

    public NullVsionManager(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void enable() {

    }

    public void end() {

    }

    public SensorSignals fetch() {
        return SensorSignals.UNKNOWN;
    }

    public void disable() {

    }
}

