package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.OrderCreateRequest;
import com.example.demo.dto.OrderCreateResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 생성
    @Operation(
            summary = "주문 생성",
            description = "사용자 ID, 상품 ID, 수량을 입력받아 주문을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @PostMapping
    public SuccessResponse<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request) {
        Long id = orderService.createOrder(
                request.getUserId(),
                request.getProductId(),
                request.getQuantity()
        );

        return new SuccessResponse<>(
                "주문이 정상적으로 진행되었습니다.",
                new OrderCreateResponse(id)
        );
    }

    // 사용자 주문 목록 조회
    @Operation(
            summary = "사용자 주문 목록 조회",
            description = "특정 사용자 ID로 주문 리스트를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자의 주문 없음")
    })
    @GetMapping
    public SuccessResponse<List<OrderResponse>> getOrders(@Parameter(description = "조회할 사용자 ID") @RequestParam Long userId) {

        // 1. 서비스에서 주문 목록 가져오기 (List<Order>)
        List<Order> orders = orderService.getOrdersByUser(userId);

        // 2. DTO 변환 (List<OrderResponse>)
        List<OrderResponse> data = orders.stream()
                .map(OrderResponse::from)
                .toList();

        // 3. 성공 응답
        return new SuccessResponse<>(
                "주문 목록이 정상적으로 조회되었습니다.",
                data
        );
    }

    // 주문 취소
    @Operation(
            summary = "주문 취소",
            description = "특정 주문 ID에 대해 주문을 취소합니다. 취소 시 재고가 복원됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 취소된 주문")
    })
    @DeleteMapping("/{orderId}")
    public SuccessResponse<OrderCreateResponse> cancelOrder(@Parameter(description = "취소할 주문 ID") @PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return new SuccessResponse<>(
                "주문이 정상적으로 취소되었습니다.",
                null
        );
    }
    
    // 주문 상세 조회
    @Operation(
            summary = "주문 상세 조회",
            description = "특정 주문 ID로 주문 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @GetMapping("/{orderId}")
    public SuccessResponse<OrderResponse> getOrder(@Parameter(description = "조회할 주문 ID") @PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return new SuccessResponse<>(
                "주문 조회 성공",
                OrderResponse.from(order)
        );
    }
}