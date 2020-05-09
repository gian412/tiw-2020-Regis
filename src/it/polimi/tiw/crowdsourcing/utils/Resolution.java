package it.polimi.tiw.crowdsourcing.utils;

public enum  Resolution {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    Resolution(int value) {
        this.value = value;
    }

    public static Resolution getResolutionFromInt(int value) {
        return switch (value) {
            case 0 -> Resolution.HIGH;
            case 1 -> Resolution.MEDIUM;
            case 2 -> Resolution.LOW;
            default -> null;
        };
    }

    public int getValue() {
        return this.value;
    }

}
