package it.polimi.tiw.crowdsourcing.utils;

public enum ExperienceLevel {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    ExperienceLevel(int value) {
        this.value = value;
    }

    public static ExperienceLevel getExperienceLevelFromInt(int value) {
        return switch (value) {
            case 0 -> ExperienceLevel.HIGH;
            case 1 -> ExperienceLevel.MEDIUM;
            case 2 -> ExperienceLevel.LOW;
            default -> null;
        };
    }

    public int getValue() {
        return this.value;
    }

}
