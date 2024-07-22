package com.example.a1201418_1200435_project;

public class Order {
    private int orderId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private int pizzaId;
    private int quantity;
    private boolean extraSauce;
    private boolean extraCheese;
    private boolean mixWithBeef;
    private String optional;

    private String orderDate;
    private double price;
    public Order (){

    }



    public Order(int orderId, String userEmail, String firstName, String lastName, int pizzaId, int quantity, boolean extraSauce, boolean extraCheese, boolean mixWithBeef, String optional, String orderDate, double price) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pizzaId = pizzaId;
        this.quantity = quantity;
        this.extraSauce = extraSauce;
        this.extraCheese = extraCheese;
        this.mixWithBeef = mixWithBeef;
        this.optional = optional;
        this.orderDate = orderDate;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isExtraSauce() {
        return extraSauce;
    }

    public void setExtraSauce(boolean extraSauce) {
        this.extraSauce = extraSauce;
    }

    public boolean isExtraCheese() {
        return extraCheese;
    }

    public void setExtraCheese(boolean extraCheese) {
        this.extraCheese = extraCheese;
    }

    public boolean isMixWithBeef() {
        return mixWithBeef;
    }

    public void setMixWithBeef(boolean mixWithBeef) {
        this.mixWithBeef = mixWithBeef;
    }

    public String getOptional() {
        return optional;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String firstName) {
        this.lastName = firstName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + "\n" +
                "User Email: " + userEmail + "\n" +
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Pizza ID: " + pizzaId + "\n" +
                "Quantity: " + quantity + "\n" +
                "Extra Sauce: " + extraSauce + "\n" +
                "Extra Cheese: " + extraCheese + "\n" +
                "Mix with Beef: " + mixWithBeef + "\n" +
                "Optional: " + optional + "\n" +
                "Order Date: " + orderDate + "\n";
    }

}
