package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public List<User> findAllUsername() throws SQLException {

        String query = "SELECT username FROM user";
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()){
                    User user = new User();
                    user.setUsername(resultSet.getString("username"));
                    users.add(user);
                }
            }
        }
        return users;

    }

    public User checkCredential(String usernameOrEmail, String password) throws SQLException {

        String query = "SELECT * FROM user WHERE (username = ? OR email = ?) AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, usernameOrEmail);
            preparedStatement.setString(2, usernameOrEmail);
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
                    user.setAvatar("/avatars/" + resultSet.getString("avatar"));
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
                    user.setAvatar("/avatars/" + resultSet.getString("avatar"));
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
                    user.setAvatar("/avatars/" + resultSet.getString("avatar"));
                    return user;
                }
            }
        }
    }

    public void createManager(String firstName, String lastName, String username, String password, String  email) throws SQLException {

        String query = "INSERT into user (role, firstname, lastname, username, password, email) VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, "manager");
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, email);

            preparedStatement.executeUpdate();
        }

    }

    public void createWorker(String firstName, String lastName, String username, String password, String  email, int experience) throws SQLException {

        String query = "INSERT into user (role, firstname, lastname, username, password, email, experience, avatar) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, "worker");
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, username);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, email);
            preparedStatement.setInt(7, experience);
            preparedStatement.setString(8, "avatar.png");
            preparedStatement.executeUpdate();
        }

    }

    public void updatePassword(int userId, String password) throws SQLException{

        String query = "UPDATE user SET password = ?  WHERE id = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }

    }

    public void deleteUser(int userId) throws SQLException {

        String query = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        }

    }

}
