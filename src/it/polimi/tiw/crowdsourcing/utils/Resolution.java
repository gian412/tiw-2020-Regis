package it.polimi.tiw.crowdsourcing.utils;

public enum  Resolution {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    Resolution(int value) {
        this.value = value;
    }

    public static Resolution getResolutionFromInt(int value) {
        switch (value) {
            case 0:
                return Resolution.HIGH;
            case 1:
                return Resolution.MEDIUM;
            case 2:
                return Resolution.LOW;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }


    @Override
    public String toString() {
        return switch (this) {
            case HIGH -> "High";
            case MEDIUM -> "Medium";
            case LOW -> "Low";
        };
    }
}
