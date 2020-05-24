package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageDAO {

    private Connection connection;

    public ImageDAO(Connection connection) {
        this.connection = connection;
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
