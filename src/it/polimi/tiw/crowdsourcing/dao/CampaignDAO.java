package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.utils.CampaignStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampaignDAO {

    private Connection connection;
    private int id;

    public CampaignDAO(Connection connection, int id) {
        this.connection = connection;
        this.id = id;
    }

    public int changeStatus(CampaignStatus status) throws SQLException {

        String query = "UPDATE campaign SET status = ? WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, status.getValue());
            preparedStatement.setInt(2, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int createAssignment(int workerId) throws SQLException {

        String query = "INSERT into assignment (workerid, campaignid) VALUES(?, ?)";
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, workerId);
            preparedStatement.setInt(2, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

}
