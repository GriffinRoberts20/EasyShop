package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCartItem addProduct(int userID, int productID);

    void updateQuantity(int userID, int productID, int quantity);

    void delete(int userID);
}
