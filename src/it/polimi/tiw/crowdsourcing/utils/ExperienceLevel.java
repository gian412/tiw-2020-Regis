package it.polimi.tiw.crowdsourcing.utils;

public enum ExperienceLevel {

    ALTO(0), MEDIO(1), BASSO(2);

    private final int value;

    ExperienceLevel(int value) {
        this.value = value;
    }

    public static ExperienceLevel getExperienceLevelFromInt(int value) {
        return switch (value) {
            case 0 -> ExperienceLevel.ALTO;
            case 1 -> ExperienceLevel.MEDIO;
            case 2 -> ExperienceLevel.BASSO;
            default -> null;
        };
    }

    public int getValue() {
        return this.value;
    }

}
