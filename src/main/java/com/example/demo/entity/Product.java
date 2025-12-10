package com.example.demo.entity;

import com.example.demo.exception.InsufficientStockException;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private int stock;

    public Product() {}

    public Product(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // 재고 감소
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new InsufficientStockException("재고가 부족합니다. (현재 재고: " + this.stock + ")");
        }
        this.stock -= quantity;
    }

    // 재고 증가
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    // Getter/Setter (필요한 만큼만 써도 됨)
    public Long getId() { return id; }

    public String getName() { return name; }

    public int getPrice() { return price; }

    public int getStock() { return stock; }

}
// decreaseStock / increaseStock은 엔티티가 스스로 책임지도록 설계
// 생성자 제공 → 테스트 및 초기 데이터 생성에 유용
