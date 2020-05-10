package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {

    private Connection connection;
    private int id;

    public WorkerDAO(Connection connection, int id) {
        this.connection = connection;
        this.id = id;
    }

    public List<Campaign> findCampaignByWorkerAssigned() throws SQLException {

        String query = "SELECT * FROM campaign C WHERE C.status = 1 AND C.id IN (SELECT campaignid FROM assignment WHERE workerid = ?)";
        List<Campaign> campaigns = new ArrayList<Campaign>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Campaign campaign = new Campaign();
                campaign.setId(resultSet.getInt("id"));
                campaign.setName(resultSet.getString("name"));
                campaign.setCustomer(resultSet.getString("customer"));
                campaign.setStatus(resultSet.getInt("status"));
                campaign.setManagerId(resultSet.getInt("managerid"));
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }

    public List<Campaign> findCampaignByWorkerNotAssigned() throws SQLException {

        String query = "SELECT * FROM campaign C WHERE C.status = 1 AND C.id NOT IN (SELECT campaignid FROM assignment WHERE workerid = ?)";
        List<Campaign> campaigns = new ArrayList<Campaign>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Campaign campaign = new Campaign();
                campaign.setId(resultSet.getInt("id"));
                campaign.setName(resultSet.getString("name"));
                campaign.setCustomer(resultSet.getString("customer"));
                campaign.setStatus(resultSet.getInt("status"));
                campaign.setManagerId(resultSet.getInt("managerid"));
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }

}
