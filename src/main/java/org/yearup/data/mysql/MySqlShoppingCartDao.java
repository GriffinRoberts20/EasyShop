package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private MySqlProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, MySqlProductDao productDao){
        super(dataSource);
        this.productDao=productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql="SELECT product_id, quantity FROM shopping_cart WHERE user_id=?";
        ShoppingCart shoppingCart=new ShoppingCart();
        try(Connection connection=getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,userId);
            ResultSet row=statement.executeQuery();
            while(row.next()){
                Product product=productDao.getById(row.getInt("product_id"));
                ShoppingCartItem shoppingCartItem=new ShoppingCartItem();
                shoppingCartItem.setProduct(product);
                shoppingCartItem.setQuantity(row.getInt("quantity"));
                shoppingCart.add(shoppingCartItem);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCartItem addProduct(int userID,int productID) {
        String sql="INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES(?,?,1)"+
                "ON DUPLICATE KEY UPDATE quantity=quantity+1";
        ShoppingCartItem shoppingCartItem=new ShoppingCartItem();
        try(Connection connection=getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,userID);
            statement.setInt(2,productID);
            int rows=statement.executeUpdate();
            if(rows>0){
                Product product=productDao.getById(productID);
                shoppingCartItem.setProduct(product);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return shoppingCartItem;
    }

    @Override
    public void updateQuantity(int userID, int productID, int quantity) {
        String sql="UPDATE shopping_cart SET quantity=? WHERE user_id=? AND product_id=?";
        try(Connection connection=getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,quantity);
            statement.setInt(2,userID);
            statement.setInt(3,productID);
            statement.executeUpdate();
            System.out.println("Updated product quantity.");
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int userID) {
        String sql="DELETE FROM shopping_cart WHERE user_id=?";
        try(Connection connection=getConnection()){
            PreparedStatement statement=connection.prepareStatement(sql);
            statement.setInt(1,userID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


}
