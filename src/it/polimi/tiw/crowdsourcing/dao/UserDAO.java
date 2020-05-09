package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static final long serialVersionUID = 1L;
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User checkCredentialUsername(String username, String password) throws SQLException {

        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setPicture(resultSet.getString("picture"));
                    return user;
                }
            }
        }

    }

    public User checkCredentialEmail(String email, String password) throws SQLException {

        String query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setPicture(resultSet.getString("picture"));
                    return user;
                }
            }
        }

    }

    public User findUserById(int userId) throws SQLException {

        String query = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setPicture(resultSet.getString("picture"));
                    return user;
                }
            }
        }
    }

    public int createUser(String role, String username, String password, String  email, ExperienceLevel experience, String picture) throws SQLException {

        String query = "INSERT into user (role, username, password, email, experience, picture) VALUES(?, ?, ?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, experience.getValue());
            preparedStatement.setString(6, picture);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int updateUsername(int userId, String username) throws SQLException {

        String query = "UPDATE user SET username = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int updatePassword(int userId, String password) throws SQLException {

        String query = "UPDATE user SET password = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int updateEmail(int userId, String email) throws SQLException {

        String query = "UPDATE user SET email = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int updateExperience(int userId, ExperienceLevel experience) throws SQLException {

        String query = "UPDATE user SET experience = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, experience.getValue());
            preparedStatement.setInt(2, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int updatePicture(int userId, String picture) throws SQLException {

        String query = "UPDATE user SET picture = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, picture);
            preparedStatement.setInt(2, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int deleteUser(int userId) throws SQLException {

        String query = "DELETE FROM user WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

}
