package it.polimi.tiw.crowdsourcing.utils;

import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClientHandler {

    public static Connection getConnection(ServletContext servletContext) throws UnavailableException {

        Connection connection;

        try {
            String driver = servletContext.getInitParameter("dbDriver");
            String url = servletContext.getInitParameter("dbUrl");
            String user = servletContext.getInitParameter("dbUser");
            String password = servletContext.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // TODO: remove after test
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            throw new UnavailableException("Couldn't get db connection");
        }
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection!=null) {
            connection.close();
        }
    }

}
