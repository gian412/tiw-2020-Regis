package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.CampaignStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignStatsDAO {

    private static final long serialVersionUID = 1L;
    private Connection connection;

    public CampaignStatsDAO(Connection connection) {
        this.connection = connection;
    }

    private int findTotalNumberOfImage(int campaignId) throws SQLException {

        String query = "SELECT COUNT(*) FROM image WHERE campaignid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }

            }
        }
        throw new SQLException();
    }

    private int findTotalNumberOfAnnotation(int campaignId) throws SQLException {

        String query = "SELECT COUNT(*) FROM annotatiom WHERE imageid IN (SELECT I.id FROM image AS I WHERE I.campaign = ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }

            }
        }
        throw new SQLException();
    }

    public CampaignStats findStatsByCampaign(int campaignId) throws SQLException {

        int totalNumberOfImage, totalNumberOfAnnotation;
        double averageNumberOfAnnotationPerImage;
        try {
            totalNumberOfImage = findTotalNumberOfImage(campaignId);
            totalNumberOfAnnotation = findTotalNumberOfAnnotation(campaignId);
            averageNumberOfAnnotationPerImage = ((double)totalNumberOfAnnotation) / ((double)totalNumberOfImage);
            // TODO: calculate conflicts
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return null;
    }
}
