package com.example.iorder.model;

public class OrderedItem {
    int id;
    String name;
    int quantity;
    float price;
    boolean isServed,isOrderAccepted;
    public OrderedItem(){}

    public OrderedItem(int id, String name, int quantity, float price, boolean isServed, boolean isOrderAccepted) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.isServed = isServed;
        this.isOrderAccepted = isOrderAccepted;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setServed(boolean served) {
        isServed = served;
    }

    public boolean isOrderAccepted() {
        return isOrderAccepted;
    }

    public void setOrderAccepted(boolean orderAccepted) {
        isOrderAccepted = orderAccepted;
    }
}
