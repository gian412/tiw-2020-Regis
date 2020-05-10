package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.CampaignStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignStatsDAO {

    private Connection connection;

    public CampaignStatsDAO(Connection connection) {
        this.connection = connection;
    }

    private int findTotalNumberOfImage(int campaignId) throws SQLException {

        String query =
                "SELECT COUNT(*) " +
                "FROM image " +
                "WHERE campaignid = ?";

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

        String query =
                "SELECT COUNT(*) " +
                "FROM annotatiom " +
                "WHERE imageid IN (" +
                        "SELECT I.id " +
                        "FROM image AS I " +
                        "WHERE I.campaign = ?" +
                ")";

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

    private int findNumberOfImageWithConflict(int campaignId) throws SQLException {

        String query =
                "SELECT COUNT(id) " +
                "FROM crowdsourcing.image AS I " +
                "WHERE I.id IN (" +
                    "SELECT A1.imageid " +
                    "FROM crowdsourcing.annotation AS A1 " +
                    "WHERE validity = 0" +
                ") AND I.id IN (" +
                    "SELECT A2.imageid " +
                    "FROM crowdsourcing.annotation AS A2 " +
                    "WHERE validity <> 0" +
                ") AND I.campaignid = ? " +
                "GROUP BY id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        throw new SQLException();
    }

    public CampaignStats findStatsByCampaign(int campaignId) throws SQLException {

        int totalNumberOfImage, totalNumberOfAnnotation, numberOfImageWithConflict;
        double averageNumberOfAnnotationPerImage;
        CampaignStats campaignStats = new CampaignStats();
        try {
            campaignStats.setTotalImage(findTotalNumberOfImage(campaignId));
            campaignStats.setTotalAnnotation(findTotalNumberOfAnnotation(campaignId));
            campaignStats.setAverageAnnotationPerImage(((double)campaignStats.getTotalAnnotation()) / ((double)campaignStats.getTotalImage()));
            campaignStats.setAnnotatedImageWithConflict(findNumberOfImageWithConflict(campaignId));
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return campaignStats;
    }
}
