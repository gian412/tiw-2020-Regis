package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Annotation;
import it.polimi.tiw.crowdsourcing.utils.Confidence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnotationDAO {

    private Connection connection;

    public AnnotationDAO(Connection connection) {
        this.connection = connection;
    }

    public int createAnnotation(int imageId, int workerId, boolean validity, Confidence confidence, String note) throws SQLException {

        String query =
                "INSERT into image (imageid, workerid, validity, confidence, note) " +
                "VALUES(?, ?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            preparedStatement.setInt(2, workerId);
            preparedStatement.setBoolean(3, validity);
            preparedStatement.setInt(4, confidence.getValue());
            preparedStatement.setString(5, note);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public List<Annotation> findAnnotationsByImage(int imageId) throws SQLException {

        String query =
                "SELECT * " +
                "FROM annotation " +
                "WHERE imageid = ?";
        List<Annotation> annotations = new ArrayList<Annotation>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Annotation annotation = new Annotation();
                    annotation.setImageId(resultSet.getInt("imageid"));
                    annotation.setWorkerId(resultSet.getInt("workerid"));
                    annotation.setValidity(resultSet.getBoolean("validity"));
                    annotation.setConfidence(resultSet.getInt("confidence"));
                    annotation.setNote(resultSet.getString("note"));
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }

}
