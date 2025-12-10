package com.example.demo.dto;

import com.example.demo.entity.OrderItem;

public class OrderItemResponse {
	// 주문 목록 조회 응답 DTO
	
	private String productName;
    private int price;
    private int quantity;

    public OrderItemResponse(String productName, int price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
    
    public String getProductName() { return productName; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity()
        );
    }
}