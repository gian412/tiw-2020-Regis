package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.CampaignStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignStatsDAO {

    private Connection connection;
    private int id;

    public CampaignStatsDAO(Connection connection, int id) {
        this.connection = connection;
        this.id = id;
    }

    private int findTotalNumberOfImage() throws SQLException {

        String query =
                "SELECT COUNT(*) " +
                "FROM image " +
                "WHERE campaignid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }

            }
        }
        throw new SQLException();
    }

    private int findTotalNumberOfAnnotation() throws SQLException {

        String query =
                "SELECT COUNT(*) " +
                "FROM annotatiom " +
                "WHERE imageid IN (" +
                        "SELECT I.id " +
                        "FROM image AS I " +
                        "WHERE I.campaign = ?" +
                ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }

            }
        }
        throw new SQLException();
    }

    private int findNumberOfImageWithConflict() throws SQLException {

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
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        throw new SQLException();
    }

    public CampaignStats findStatsByCampaign() throws SQLException {

        int totalNumberOfImage, totalNumberOfAnnotation, numberOfImageWithConflict;
        double averageNumberOfAnnotationPerImage;
        CampaignStats campaignStats = new CampaignStats();
        try {
            campaignStats.setTotalImage( findTotalNumberOfImage() );
            campaignStats.setTotalAnnotation( findTotalNumberOfAnnotation() );
            campaignStats.setAverageAnnotationPerImage(((double)campaignStats.getTotalAnnotation()) / ((double)campaignStats.getTotalImage()));
            campaignStats.setAnnotatedImageWithConflict( findNumberOfImageWithConflict() );
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return campaignStats;
    }
}
