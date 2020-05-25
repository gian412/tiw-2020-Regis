package it.polimi.tiw.crowdsourcing.dao;

import it.polimi.tiw.crowdsourcing.beans.Campaign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnonymousCampaignDAO {

    private Connection connection;

    public AnonymousCampaignDAO(Connection connection) {
        this.connection = connection;
    }

    public Campaign findCampaignById(int campaignId) throws SQLException {

        String query =
                "SELECT * " +
                        "FROM campaign " +
                        "WHERE id = ?";
        Campaign campaign = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, campaignId);
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
}
