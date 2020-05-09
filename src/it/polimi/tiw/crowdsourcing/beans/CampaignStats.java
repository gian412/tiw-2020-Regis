package it.polimi.tiw.crowdsourcing.beans;

public class CampaignStats {

    private int totalImage;
    private int totalAnnotation;
    private double averageAnnotationPerImage;
    private int annotatedImageWithConflict;

    public int getTotalImage() {
        return totalImage;
    }

    public void setTotalImage(int totalImage) {
        this.totalImage = totalImage;
    }

    public int getTotalAnnotation() {
        return totalAnnotation;
    }

    public void setTotalAnnotation(int totalAnnotation) {
        this.totalAnnotation = totalAnnotation;
    }

    public double getAverageAnnotationPerImage() {
        return averageAnnotationPerImage;
    }

    public void setAverageAnnotationPerImage(double averageAnnotationPerImage) {
        this.averageAnnotationPerImage = averageAnnotationPerImage;
    }

    public int getAnnotatedImageWithConflict() {
        return annotatedImageWithConflict;
    }

    public void setAnnotatedImageWithConflict(int annotatedImageWithConflict) {
        this.annotatedImageWithConflict = annotatedImageWithConflict;
    }
}
