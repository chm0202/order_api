package com.example.demo.exception;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(Long userId) {
        super("해당 사용자의 주문이 없습니다. (userId=" + userId + ")");
    }
}