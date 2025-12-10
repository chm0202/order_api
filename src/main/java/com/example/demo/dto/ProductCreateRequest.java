package com.example.demo.dto;

public class ProductCreateRequest {

    private String name;
    private int price;
    private int stock;
    
    public ProductCreateRequest() {}

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}