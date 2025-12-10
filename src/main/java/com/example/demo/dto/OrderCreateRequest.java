package com.example.demo.dto;

public class OrderCreateRequest {
	// 주문 생성 요청 DTO
	
	private Long userId;
    private Long productId;
    private int quantity;

    public Long getUserId() { return userId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}