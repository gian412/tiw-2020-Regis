package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.utils.CampaignStatus;
import it.polimi.tiw.crowdsourcing.utils.Resolution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CampaignDAO {

    private Connection connection;
    private int id;

    public CampaignDAO(Connection connection, int id) {
        this.connection = connection;
        this.id = id;
    }

    public int findNumberOfImageByCampaign() throws SQLException {

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

    public int editCampaign(String name, String costumer) throws SQLException {

        String query =
                "UPDATE campaign " +
                "SET name = ?, customer = ? " +
                "WHERE id = ?";

        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, costumer);
            preparedStatement.setInt(3, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;

    }

    public int changeStatus(CampaignStatus status) throws SQLException {

        String query =
                "UPDATE campaign " +
                "SET status = ? " +
                "WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, status.getValue());
            preparedStatement.setInt(2, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int createAssignment(int workerId) throws SQLException {

        String query =
                "INSERT into assignment (workerid, campaignid) " +
                "VALUES(?, ?)";
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, workerId);
            preparedStatement.setInt(2, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int addImage(String source, double latitude, double longitude, String city, String region,
                           String provenance, Date date, Resolution resolution) throws SQLException {

        String query = "INSERT into image (source, latitude, longitude, city, region, provenance, date, resolution, campaignid) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, source);
            preparedStatement.setDouble(2, latitude);
            preparedStatement.setDouble(3, longitude);
            preparedStatement.setString(4, city);
            preparedStatement.setString(5, region);
            preparedStatement.setString(6, provenance);
            preparedStatement.setDate(7, new java.sql.Date(date.getTime()));
            preparedStatement.setInt(8, resolution.getValue());
            preparedStatement.setInt(9, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public List<Image> findImagesByCampaign() throws SQLException {

        String query = "SELECT * FROM image WHERE campaignid = ?";
        List<Image> images = new ArrayList<Image>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()){
                    Image image = new Image();
                    image.setId(resultSet.getInt("id"));
                    image.setSource("/var/webapps/uploads/images/" + resultSet.getString("source"));// http://localhost:8080/images/
                    image.setLatitude(resultSet.getDouble("latitude"));
                    image.setLongitude(resultSet.getDouble("longitude"));
                    image.setCity(resultSet.getString("city"));
                    image.setRegion(resultSet.getString("region"));
                    image.setProvenance(resultSet.getString("provenance"));
                    image.setDate(resultSet.getDate("date"));
                    image.setResolution(resultSet.getInt("resolution"));
                    image.setCampaignId(resultSet.getInt("campaignid"));
                    images.add(image);
                }
            }
        }
        return images;
    }

}
