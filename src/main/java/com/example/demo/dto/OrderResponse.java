package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Order;

public class OrderResponse {
	// 주문 조회 응답 DTO
	
	private Long userId;
    private Long orderId;
    private String status;
    private List<OrderItemResponse> items;

    public OrderResponse(Long userId, Long orderId, String status, List<OrderItemResponse> items) {
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
        this.items = items;
    }

    public Long getUserId() { return userId; }
    public Long getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public List<OrderItemResponse> getItems() { return items; }
    
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getUserId(),
                order.getId(),
                order.getStatus().name(),
                order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}