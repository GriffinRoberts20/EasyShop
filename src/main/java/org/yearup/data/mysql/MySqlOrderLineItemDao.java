package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OrderLineItem add(OrderLineItem orderLineItem) {
        String sql="INSERT INTO order_line_items(order_id, product_id, sales_price, quantity, discount) VALUES (?,?,?,?,?)";
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,orderLineItem.getOrderId());
            statement.setInt(2,orderLineItem.getProductId());
            statement.setBigDecimal(3,orderLineItem.getSalesPrice());
            statement.setInt(4,orderLineItem.getQuantity());
            statement.setDouble(5,orderLineItem.getDiscount());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderLineItemId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderLineItemId);
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
    public OrderLineItem getById(int orderLineItemId) {
        String sql="SELECT * FROM order_line_items WHERE order_line_item_id=?";
        try (Connection connection = getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,orderLineItemId);
            ResultSet result= statement.executeQuery();
            while(result.next()){
                OrderLineItem orderLineItem=new OrderLineItem();
                orderLineItem.setOrderLineItemId(result.getInt("order_line_item_id"));
                orderLineItem.setOrderId(result.getInt("order_id"));
                orderLineItem.setProductId(result.getInt("product_id"));
                orderLineItem.setSalesPrice(result.getBigDecimal("sales_price"));
                orderLineItem.setQuantity(result.getInt("quantity"));
                orderLineItem.setDiscount(result.getDouble("discount"));
                return orderLineItem;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }
}
