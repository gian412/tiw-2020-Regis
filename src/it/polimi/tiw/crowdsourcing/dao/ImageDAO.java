package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.utils.Resolution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageDAO {

    private Connection connection;

    public ImageDAO(Connection connection) {
        this.connection = connection;
    }

    public int createImage(String source, String thumbnail, double latitude, double longitude, String city, String region,
                           String provenance, Date date, Resolution resolution, int campaignId) throws SQLException {

        String query = "INSERT into image (source, thumbnail, latitude, longitude, city, region, provenance, date, resolution, campaignid) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, source);
            preparedStatement.setString(2, thumbnail);
            preparedStatement.setDouble(3, latitude);
            preparedStatement.setDouble(4, longitude);
            preparedStatement.setString(5, city);
            preparedStatement.setString(6, region);
            preparedStatement.setString(7, provenance);
            preparedStatement.setDate(8, new java.sql.Date(date.getTime()));
            preparedStatement.setInt(9, resolution.getValue());
            preparedStatement.setInt(10, campaignId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public Image findThumbnailByCampaign(int campaignId) throws SQLException {

        String query = "SELECT id, thumbnail FROM image WHERE campaignid = ?";
        Image image = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return null;
                } else {
                    resultSet.next();
                    image = new Image();
                    image.setId(resultSet.getInt("id"));
                    image.setThumbnail(resultSet.getString("thumbnail"));
                    return image;
                }
            }
        }
    }

    public List<Image> findImagesByCampaign(int campaignId) throws SQLException {

        String query = "SELECT * FROM image WHERE campaignid = ?";
        List<Image> images = new ArrayList<Image>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()){
                    Image image = new Image();
                    image.setId(resultSet.getInt("id"));
                    image.setSource(resultSet.getString("source"));
                    image.setThumbnail(resultSet.getString("thumbnail"));
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

    public Image findImageById(int imageId) throws SQLException {

        String query = "SELECT * FROM image WHERE id = ?";
        Image image = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return null;
                } else {
                    resultSet.next();
                    image = new Image();
                    image.setId(resultSet.getInt("id"));
                    image.setSource(resultSet.getString("source"));
                    image.setThumbnail(resultSet.getString("thumbnail"));
                    image.setLatitude(resultSet.getDouble("latitude"));
                    image.setLongitude(resultSet.getDouble("longitude"));
                    image.setCity(resultSet.getString("city"));
                    image.setRegion(resultSet.getString("region"));
                    image.setProvenance(resultSet.getString("provenance"));
                    image.setDate(resultSet.getDate("date"));
                    image.setResolution(resultSet.getInt("resolution"));
                    image.setCampaignId(resultSet.getInt("campaignid"));
                    return image;
                }
            }
        }
    }

}
