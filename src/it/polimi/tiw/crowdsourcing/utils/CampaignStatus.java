package it.polimi.tiw.crowdsourcing.utils;

public enum CampaignStatus {

    CREATED(0), STARTED(1), CLOSED(2);

    private final int value;

    CampaignStatus(int value) {
        this.value = value;
    }

    public static CampaignStatus getCampaignStatusFromInt(int value) {
        return switch (value) {
            case 0 -> CampaignStatus.CREATED;
            case 1 -> CampaignStatus.STARTED;
            case 2 -> CampaignStatus.CLOSED;
            default -> null;
        };
    }

    public int getValue() {
        return this.value;
    }

}
