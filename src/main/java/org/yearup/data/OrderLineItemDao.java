package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

public interface OrderLineItemDao {
    OrderLineItem add(OrderLineItem orderLineItem);
    OrderLineItem getById(int orderLineItemId);
}
