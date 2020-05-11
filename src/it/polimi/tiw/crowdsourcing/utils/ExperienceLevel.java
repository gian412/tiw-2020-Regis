package it.polimi.tiw.crowdsourcing.utils;

public enum ExperienceLevel {

    HIGH(0), MEDIUM(1), LOW(2);

    private final int value;

    ExperienceLevel(int value) {
        this.value = value;
    }

    public static ExperienceLevel getExperienceLevelFromInt(int value) {
        switch (value) {
            case 0:
                return ExperienceLevel.HIGH;
            case 1:
                return ExperienceLevel.MEDIUM;
            case 2:
                return ExperienceLevel.LOW;
            default:
                return null;
        }
    }

    public int getValue() {
        return this.value;
    }

}
