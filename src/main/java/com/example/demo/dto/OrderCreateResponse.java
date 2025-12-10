package com.example.demo.dto;

public class OrderCreateResponse {
	// 주문 생성 응답 DTO
	
	private Long orderId;

    public OrderCreateResponse(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}