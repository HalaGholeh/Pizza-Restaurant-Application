package com.example.a1201418_1200435_project;

public class Offer {
    int id;
    String date;
    int period;
    double discount;
    int pizzaId;

    public Offer(int id, String date, int period, double discount, int pizzaId) {
        this.id = id;
        this.date = date;
        this.period = period;
        this.discount = discount;
        this.pizzaId = pizzaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getDate() {
        return date;
    }

    public int getPeriod() {
        return period;
    }

    public double getDiscount() {
        return discount;
    }

    public int getPizzaId() {
        return pizzaId;
    }
}
