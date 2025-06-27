# EasyShop
## Application Summary
The application is an e-commerce backend system called EasyShop, built using Java with Spring Boot and a MySQL database. It simulates an online store where users can:

Register and log in (with roles like USER and ADMIN)

Browse and search products by category, price, or color

Add products to a shopping cart

Place orders at checkout

Admins can manage product and category data

### Spring Boot REST API Functionality
The backend API is implemented using Spring Boot and follows standard RESTful practices, with clear separation of concerns using:

Controllers – handle HTTP requests and return JSON responses

DAOs (Data Access Objects) – interface with the MySQL database

Models – represent the data (e.g., Product, Category, User, etc.)

### Authentication
POST /register – creates new user accounts

POST /login – returns a JWT token for authenticated access

### Product & Category Management
GET /products – list or search products using filters like category, price, and color

GET /products/{id} – view a specific product

POST/PUT/DELETE /products – admins only; create, update, delete products

GET /categories – list all categories

POST/PUT/DELETE /categories – admins only; manage categories

### Shopping Cart
GET /cart – view logged-in user’s cart

POST /cart/products/{id} – add product to cart

PUT /cart/products/{id} – update quantity

DELETE /cart – clear cart

### User Profile
GET /profile – view profile info

PUT /profile – update profile info

### Checkout
POST /orders – convert cart to order and clear cart

### Development Tasks
Fix bugs in product filtering/search and product update logic

Implement missing controller methods (especially for categories and cart)

Use Postman to test endpoints

Demonstrate via a front-end and/or API testing tools

## Application Screens

### Home Screen
On the home screen, you can choose from several menus, filter by color, price, and category, add items to your cart, and log in.

![image](https://github.com/user-attachments/assets/354d06f8-e3c1-48f0-ab2a-d3d8be7af107)

### Profile Screen
On the profile screen, you can update the information of the account currently logged in to.

![image](https://github.com/user-attachments/assets/e9be1021-9ac4-412f-9c47-e531c096fb34)

### Shopping Cart Screen
On the shopping cart screen, you can review the items you've added to your cart and clear your shopping cart.

![image](https://github.com/user-attachments/assets/16621424-b715-4ca6-8cbf-38d01114a691)

## Interesting Code
```Java
@PostMapping("")
    public Order checkout(Principal principal){
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart shoppingCart=shoppingCartDao.getByUserId(userId);
            if(shoppingCart.getItems().isEmpty()) return null;
            Profile profile=profileDao.getByUserId(userId);
            AtomicInteger shippingTotal= new AtomicInteger();
            shoppingCart.getItems().forEach((key,ShoppingCartItem)->{
                shippingTotal.getAndIncrement();
            });
            Order order=new Order(profile,shippingTotal.doubleValue());
            order=orderDao.add(order);
            Order finalOrder = order;
            shoppingCart.getItems().forEach((key, ShoppingCartItem)->{
                Product product=ShoppingCartItem.getProduct();
                OrderLineItem orderLineItem=new OrderLineItem(finalOrder.getOrderId(),product.getProductId(),product.getPrice(),ShoppingCartItem.getQuantity());
                orderLineItemDao.add(orderLineItem);
            });
            shoppingCartDao.delete(userId);
            return order;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
```



