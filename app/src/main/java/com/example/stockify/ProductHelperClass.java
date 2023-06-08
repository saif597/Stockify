package com.example.stockify;

public class ProductHelperClass {
    private String name;
    private String description;
    private int discount;
    private String category;

    private int quantity;
    private double price;
    private String imageUrl;

    public ProductHelperClass() {
        // Empty constructor needed for Firebase Realtime Database
    }

    public ProductHelperClass(String name, String description, int quantity, double price, String imageUrl,int discount,String category) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discount=discount;
        this.category=category;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }




    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}