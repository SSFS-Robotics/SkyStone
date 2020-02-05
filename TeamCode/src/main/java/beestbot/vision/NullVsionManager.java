package beestbot.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import beestbot.state.SensorSignals;

public class NullVsionManager implements VisionManager {

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public NullVsionManager(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
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

    @Override
    public void init() {

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
}

