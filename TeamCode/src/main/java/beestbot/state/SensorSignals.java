package beestbot.state;

public enum SensorSignals {
    STONE(1),
    SKYSTONE(2),
    NOTHING(0),
    UNKNOWN(-1);

    public final int value;
    SensorSignals(int i) {
        this.value = i;
    }
    public int getValue() {
        return value;
    }

    public static SensorSignals getSensorSignals(int value) {
        for (SensorSignals l : SensorSignals.values()) {
            if (l.value == value) return l;
        }
        throw new IllegalArgumentException("Invalid SensorSignal value");
    }
}