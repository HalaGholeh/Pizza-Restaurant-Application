package com.example.a1201418_1200435_project;
public class Favorite {
    private int favoriteId;
    private String userEmail;
    private int pizzaId;

    public Favorite(int favoriteId, String userEmail, int pizzaId) {
        this.favoriteId = favoriteId;
        this.userEmail = userEmail;
        this.pizzaId = pizzaId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
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
}
