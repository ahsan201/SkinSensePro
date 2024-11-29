package com.example.skinsensepro;

import java.io.Serializable;
import java.util.Map;

public class Product implements Serializable {
    private String productName;
    private String productCategory;
    private String productDescription;
    private String productImage;
    private String productPrice;
    private int productAverageRateing;
    private Map<String, Integer> productIngredient; // Field for ingredients
    private String productLink; // Product link
    private String productUid; // Firestore document ID

    public Product() {
        // Required empty public constructor
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductAverageRateing() {
        return productAverageRateing;
    }

    public void setProductAverageRateing(int productAverageRateing) {
        this.productAverageRateing = productAverageRateing;
    }

    public Map<String, Integer> getProductIngredient() {
        return productIngredient;
    }

    public void setProductIngredient(Map<String, Integer> productIngredient) {
        this.productIngredient = productIngredient;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductUid() {
        return productUid;
    }

    public void setProductUid(String productUid) {
        this.productUid = productUid;
    }
}
