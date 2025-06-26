package org.yearup.data;

import org.yearup.models.Order;

public interface OrderDao {
    Order add(Order order);
    Order getById(int orderId);
}
