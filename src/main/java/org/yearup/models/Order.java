package org.yearup.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int userId;
    private Timestamp dateTime;
    private String address;
    private String city;
    private String state;
    private String zip;
    private double shippingAmount;

    public Order(int orderId, int userId, String address, String city, String state, String zip, double shippingAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.shippingAmount = shippingAmount;
        this.dateTime=currentDate();
    }

    public Order() {
        this.dateTime=currentDate();
    }

    public Order(Profile profile, double shippingAmount){
        this.userId=profile.getUserId();
        this.address=profile.getAddress();
        this.city=profile.getCity();
        this.state=profile.getState();
        this.zip=profile.getZip();
        this.shippingAmount=shippingAmount;
        this.dateTime=currentDate();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public Timestamp currentDate(){
        LocalDateTime localDateTime=LocalDateTime.now();
        return Timestamp.valueOf(localDateTime);
    }
}
