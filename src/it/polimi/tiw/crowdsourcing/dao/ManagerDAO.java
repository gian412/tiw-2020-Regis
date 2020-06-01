package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.utils.CampaignStatus;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerDAO {

    private Connection connection;
    private int id;

    public ManagerDAO(Connection connection, int id) {
        this.connection = connection;
        this.id = id;
    }

    public List<Campaign> findCampaignsByManager() throws SQLException {

        String query = "SELECT * FROM campaign WHERE managerid = ? ORDER BY name DESC ";
        List<Campaign> campaigns = new ArrayList<Campaign>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.id);
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

    public Campaign findCampaignByName(String campaignName) throws SQLException {

        String query =
                "SELECT * " +
                "FROM campaign " +
                "WHERE name = ?";
        Campaign campaign = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, campaignName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return null;
                } else {
                    resultSet.next();
                    campaign = new Campaign();
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

    public void createCampaign(String name, String customer) throws SQLException {
        String query = "INSERT into campaign (name, customer, status, managerid) VALUES(?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, customer);
            preparedStatement.setInt(3, CampaignStatus.CREATED.getValue());
            preparedStatement.setInt(4, this.id);
            preparedStatement.executeUpdate();
        }

    }

    public void updateManager(String firstName, String lastName, String username, String email) throws SQLException{

        String query =
                "UPDATE user " +
                        "SET firstname = ?, lastname = ?, username = ?, email = ?  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, this.id);
            preparedStatement.executeUpdate();
        }

    }

}
