package com.example.demo.exception;

public class AlreadyCanceledException extends RuntimeException {
    public AlreadyCanceledException(Long orderId) {
        super("이미 취소된 주문입니다. (orderId=" + orderId + ")");
    }
}