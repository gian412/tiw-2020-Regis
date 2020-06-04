package it.polimi.tiw.crowdsourcing.beans;

import it.polimi.tiw.crowdsourcing.utils.Confidence;

import java.util.Date;

public class Annotation {

    private int id;
    private int imageId;
    private int workerId;
    private String workerUsername;
    private String workerAvatar;
    private boolean validity;
    private Confidence confidence;
    private String note;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getWorkerUsername() {
        return workerUsername;
    }

    public void setWorkerUsername(String workerUsername) {
        this.workerUsername = workerUsername;
    }

    public String getWorkerAvatar() {
        return workerAvatar;
    }

    public void setWorkerAvatar(String workerAvatar) {
        this.workerAvatar = workerAvatar;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public Confidence getConfidence() {
        return confidence;
    }

    public void setConfidence(int value) {
        this.confidence = Confidence.getConfidenceFromInt(value);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValidity() {
        if (this.isValidity()) {
            return "Yes";
        }
        return "No";
    }

}
