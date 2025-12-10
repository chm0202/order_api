package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 입력 또는 유효하지 않은 요청 (상품 ID 잘못됨 등)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidInput(InvalidInputException e) {
        ApiErrorResponse response = new ApiErrorResponse(
                "INVALID_INPUT",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 이미 취소된 주문인데 또 취소하려 할 때
    @ExceptionHandler(AlreadyCanceledException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyCanceled(AlreadyCanceledException e) {
        ApiErrorResponse response = new ApiErrorResponse(
                "ALREADY_CANCELED",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 주문을 찾을 수 없음
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotFound(OrderNotFoundException e) {
        ApiErrorResponse response = new ApiErrorResponse(
                "ORDER_NOT_FOUND",
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    // 구조 상 user 테이블이 없음 고로 존재하지 않는 userId가 판단이 안됨
    // 그래서 userId에 해당하는 주문이 한 건도 없다면 예외처리
    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoOrderFound(NoOrderFoundException e) {

        ApiErrorResponse response = new ApiErrorResponse(
                "NO_ORDERS",
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    // 재고 부족
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientStock(InsufficientStockException e) {

        ApiErrorResponse response = new ApiErrorResponse(
                "INSUFFICIENT_STOCK",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 그 외 예상하지 못한 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception e) {
        ApiErrorResponse response = new ApiErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}