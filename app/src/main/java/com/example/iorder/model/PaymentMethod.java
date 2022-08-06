package com.example.iorder.model;

public class PaymentMethod {
    int id;
    String method;
    String information;
    public PaymentMethod(){}

    public int getId() {
        return id;
    }

    public PaymentMethod(int id, String method, String information) {
        this.id = id;
        this.method = method;
        this.information = information;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
