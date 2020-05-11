package it.polimi.tiw.crowdsourcing.utils;

public enum Confidence {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    Confidence(int value) {
        this.value = value;
    }

    public static Confidence getConfidenceFromInt(int value) {
        switch (value) {
            case 0:
                return Confidence.HIGH;
            case 1:
                return Confidence.MEDIUM;
            case 2:
                return Confidence.LOW;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }

}
