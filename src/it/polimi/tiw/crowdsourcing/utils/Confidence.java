package it.polimi.tiw.crowdsourcing.utils;

public enum Confidence {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    Confidence(int value) {
        this.value = value;
    }

    public static Confidence getConfidenceFromInt(int value) {
        return switch (value) {
            case 0 -> Confidence.HIGH;
            case 1 -> Confidence.MEDIUM;
            case 2 -> Confidence.LOW;
            default -> null;
        };
    }

    public int getValue() {
        return this.value;
    }

}
