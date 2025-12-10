package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int price;

    private int quantity;

    public OrderItem() {}

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.price = product.getPrice();
        this.quantity = quantity;
    }

    // 주문 취소 시 재고 원복
    public void restoreStock() {
        product.increaseStock(quantity);
    }

    // 연관관계 편의 메서드
    void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
    public Product getProduct() { return product; }
}
// OrderItem은 주문 당시 가격(price)을 저장해야 한다
// restoreStock() → Order.cancel()에서 호출됨
// setOrder()는 Order에서 관리하는 관계 설정용 내부 메서드