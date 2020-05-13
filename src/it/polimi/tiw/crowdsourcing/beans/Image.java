package it.polimi.tiw.crowdsourcing.beans;

import it.polimi.tiw.crowdsourcing.utils.Resolution;

import java.util.Date;

public class Image {

    private int id;
    private String source;
    private double latitude;
    private double longitude;
    private String city;
    private String region;
    private String provenance;
    private Date date;
    private Resolution resolution;
    private int campaignId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(int value) {
        this.resolution = Resolution.getResolutionFromInt(value);
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

}
