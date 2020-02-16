package beestbot.state;

public enum SensorSignals {
    UNKNOWN(-1),
    NOTHING(0),
    STONE(1),
    SKYSTONE(2),
    SKYSTONE_AT_ONE(3),
    SKYSTONE_AT_TWO(4),
    SKYSTONE_AT_THREE(5),
    SKYSTONE_AT_FOUR(6),
    SKYSTONE_AT_FIVE(7),
    SKYSTONE_AT_SIX(8);

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