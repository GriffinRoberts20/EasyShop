package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order add(Order order) {
        String sql="INSERT INTO orders(user_id, date, address, city, state, zip, shipping_amount) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,order.getUserId());
            statement.setTimestamp(2,order.getDateTime());
            statement.setString(3,order.getAddress());
            statement.setString(4, order.getCity());
            statement.setString(5, order.getState());
            statement.setString(6, order.getZip());
            statement.setDouble(7,order.getShippingAmount());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Order getById(int orderId) {
        String sql="SELECT * FROM orders WHERE order_id=?";
        try (Connection connection = getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,orderId);
            ResultSet result= statement.executeQuery();
            while(result.next()){
                Order order=new Order();
                order.setOrderId(result.getInt("order_id"));
                order.setUserId(result.getInt("user_id"));
                order.setDateTime(result.getTimestamp("date"));
                order.setAddress(result.getString("address"));
                order.setCity(result.getString("city"));
                order.setState(result.getString("state"));
                order.setZip(result.getString("zip"));
                order.setShippingAmount(result.getDouble("shipping_amount"));
                return order;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
}
