package com.example.stockify;

public class SaleHelperClass {

    public String productName;


    public String productId;
    public int quantity;
    public String date;
    public double revenue;

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double profit;
    public double totalPrice;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



        public SaleHelperClass(String productId, int quantity, double revenue, String date, String productName, double totalPrice,double profit) {
            this.productId = productId;
            this.quantity = quantity;
            this.revenue = revenue;
            this.date = date;
            this.profit=profit;
            this.productName = productName;
            this.totalPrice = totalPrice;
        }

        // Getters and setters for the sale data fields

}
