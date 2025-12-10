package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.NoOrderFoundException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // 주문 생성
    @Transactional
    public Long createOrder(Long userId, Long productId, int quantity) {

        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InvalidInputException("상품을 찾을 수 없습니다."));

        // 재고 체크 + 감소
        product.decreaseStock(quantity);

        // OrderItem 생성
        OrderItem orderItem = new OrderItem(product, quantity);

        // Order 생성
        Order order = new Order(userId);
        order.addOrderItem(orderItem);

        // 저장 (Order만 저장하면 OrderItem도 함께 저장됨)
        orderRepository.save(order);

        return order.getId();
    }

    // 사용자 주문 목록 조회
    public List<Order> getOrdersByUser(Long userId) {
    	List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new NoOrderFoundException(userId);
        }

        return orders;
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.cancel(); // 상태 변경 + 재고 복구
    }
    
    // 주문 상세 조회
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}