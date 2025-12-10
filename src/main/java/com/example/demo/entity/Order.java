package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.exception.AlreadyCanceledException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Order(Long userId) {
        this.userId = userId;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    // 연관된 OrderItem 추가하는 편의 메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // 주문 취소 시 재고 복구
    public void cancel() {
        if (this.status == OrderStatus.CANCELED) {
        	throw new AlreadyCanceledException(this.id);
        }
        this.status = OrderStatus.CANCELED;

        for (OrderItem item : orderItems) {
            item.restoreStock();  // Product 재고 복구
        }
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public OrderStatus getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<OrderItem> getOrderItems() { return orderItems; }
}
// Order 생성 시 상태 = CREATED 자동 설정
// cancel() 메서드에서 OrderItem을 순회하며 재고 복구
// addOrderItem() 사용해서 양방향 관계를 안정적으로 설정