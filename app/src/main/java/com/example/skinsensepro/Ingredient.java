package com.example.skinsensepro;

public class Ingredient {
    private String name;
    private int riskRating;

    public Ingredient(String name, int riskRating) {
        this.name = name;
        this.riskRating = riskRating;
    }

    public String getName() {
        return name;
    }

    public int getRiskRating() {
        return riskRating;
    }
}
