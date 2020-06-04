package it.polimi.tiw.crowdsourcing.utils;

public enum CampaignStatus {

    CREATED(0), STARTED(1), CLOSED(2);

    private final int value;

    CampaignStatus(int value) {
        this.value = value;
    }

    public static CampaignStatus getCampaignStatusFromInt(int value) {
        switch (value) {
            case 0:
                return CampaignStatus.CREATED;
            case 1:
                return CampaignStatus.STARTED;
            case 2:
                return CampaignStatus.CLOSED;
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
            case CREATED -> "CREATED";
            case STARTED -> "STARTED";
            case CLOSED -> "CLOSED";
        };
    }
}