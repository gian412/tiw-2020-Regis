package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User checkCredential(String username, String password) throws SQLException {

        String query = "SELECT * FROM user WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("firstname"));
                    user.setLastName(resultSet.getString("lastname"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setAvatar(resultSet.getString("avatar"));
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
                    user.setFirstName(resultSet.getString("firstname"));
                    user.setLastName(resultSet.getString("lastname"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
    }

    public User findUserByUsername(String username) throws SQLException {

        String query = "SELECT * FROM user WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("firstname"));
                    user.setLastName(resultSet.getString("lastname"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
    }

    public User findUserByEmail(String email) throws SQLException {

        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) { // No result, credential check failed
                    return null;
                } else {
                    resultSet.next();
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("firstname"));
                    user.setLastName(resultSet.getString("lastname"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setExperienceLevel(resultSet.getInt("experience"));
                    user.setAvatar(resultSet.getString("avatar"));
                    return user;
                }
            }
        }
    }

    public int createManager(String role, String firstName, String lastName, String username, String password, String  email) throws SQLException {

        String query = "INSERT into user (role, firstname, lastname, username, password, email) VALUES(?, ?, ?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, email);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int createWorker(String role, String firstName, String lastName, String username, String password, String  email, int experience, String avatar) throws SQLException {

        String query = "INSERT into user (role, firstname, lastname, username, password, email, experience, avatar) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, email);
            preparedStatement.setInt(7, experience);
            preparedStatement.setString(8, avatar);
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

    public int updateAvatar(int userId, String avatar) throws SQLException {

        String query = "UPDATE user SET avatar = ?  WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, avatar);
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
