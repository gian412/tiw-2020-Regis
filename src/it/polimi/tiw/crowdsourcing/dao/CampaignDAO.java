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

    private static final long serialVersionUID = 1L;
    private Connection connection;

    public CampaignDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Campaign> findCampaignsByManager(int managerId) throws SQLException {

        String query = "SELECT * FROM campaign WHERE managerid = ? ORDER BY name DESC ";
        List<Campaign> campaigns = new ArrayList<Campaign>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, managerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Campaign campaign = new Campaign();
                    campaign.setId(resultSet.getInt("id"));
                    campaign.setName(resultSet.getString("name"));
                    campaign.setCustomer(resultSet.getString("customer"));
                    campaign.setStatus(resultSet.getInt("status"));
                    campaign.setManagerId(resultSet.getInt("managerid"));
                    campaigns.add(campaign);
                }
            }
        }
        return campaigns;
    }

    public int createCampaign(String name, String customer, int managerId) throws SQLException {
        String query = "INSERT into campaign (name, customer, status, managerid) VALUES(?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, customer);
            preparedStatement.setInt(3, CampaignStatus.CREATED.getValue());
            preparedStatement.setInt(4, managerId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public int changeStatus(int campaignId, CampaignStatus status) throws SQLException {

        String query = "UPDATE campaign SET status = ? WHERE id = ?";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, status.getValue());
            preparedStatement.setInt(2, campaignId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public List<Campaign> findCampaignByWorkerAssigned(int workerId) throws SQLException {

        String query = "SELECT * FROM campaign C WHERE C.status = 1 AND C.id IN (SELECT campaignid FROM assignment WHERE workerid = ?)";
        List<Campaign> campaigns = new ArrayList<Campaign>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, workerId);
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

    public List<Campaign> findCampaignByWorkerNotAssigned(int workerId) throws SQLException {

        String query = "SELECT * FROM campaign C WHERE C.status = 1 AND C.id NOT IN (SELECT campaignid FROM assignment WHERE workerid = ?)";
        List<Campaign> campaigns = new ArrayList<Campaign>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, workerId);
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

    public int createAssignment(int workerId, int campaignId) throws SQLException {

        String query = "INSERT into assignment (workerid, campaignid) VALUES(?, ?)";
        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, workerId);
            preparedStatement.setInt(2, campaignId);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

    public Campaign findCampaignById(int campaignId) throws SQLException {

        String query = "SELECT * FROM campaign WHERE campaignid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return null;
                } else {
                    resultSet.next();
                    Campaign campaign = new Campaign();
                    campaign.setId(resultSet.getInt("id"));
                    campaign.setName(resultSet.getString("name"));
                    campaign.setCustomer(resultSet.getString("customer"));
                    campaign.setStatus(resultSet.getInt("status"));
                    campaign.setManagerId(resultSet.getInt("managerid"));
                    return campaign;
                }
            }
        }

    }

}
