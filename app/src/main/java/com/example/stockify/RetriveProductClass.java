package com.example.stockify;

public class RetriveProductClass  {
    private String name;
    private String description;
    private int discount;
    private String productId;
    private String category;

    private int quantity;
    private double price;
    private String imageUrl;

    public RetriveProductClass() {
        // Empty constructor needed for Firebase Realtime Database
    }

    public RetriveProductClass(String productId,String name, String description,String imageUrl,String category, int quantity, double price, int discount) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discount=discount;
        this.productId=productId;
        this.category=category;
    }


    public String getProductId() {
        return productId;
    }
    public void setProductId(String name) {
        this.productId = productId;
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