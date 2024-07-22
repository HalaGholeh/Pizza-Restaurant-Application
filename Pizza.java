package com.example.a1201418_1200435_project;
public class Pizza {
    private int id;
    private String name;
    private String category;
    private double price;
    private String size;

    private int orderCount;
    private double totalIncome;

    public Pizza() {

    }


    public Pizza(String name, int orderCount, double totalIncome) {
        this.name = name;
        this.orderCount = orderCount;
        this.totalIncome = totalIncome;
    }

    public Pizza(String name, String category, double price, String size) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.size = size;
    }

    public Pizza(int id, String name, String category, double price, String size) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.size = size;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getOrderCount(){return orderCount;}

    public double getTotalIncome(){return totalIncome;}

    @Override
    public String toString() {
        return name;
    }
}
