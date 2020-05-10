package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.utils.CampaignStatus;

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

    public int createCampaign(String name, String customer) throws SQLException {
        String query = "INSERT into campaign (name, customer, status, managerid) VALUES(?, ?, ?, ?)";
        int result = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, customer);
            preparedStatement.setInt(3, CampaignStatus.CREATED.getValue());
            preparedStatement.setInt(4, this.id);
            result = preparedStatement.executeUpdate();
        }
        return result;
    }

}