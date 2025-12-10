package com.example.demo.controller;

import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 목록 조회
    @Operation(
            summary = "상품 목록 조회",
            description = "등록된 모든 상품의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public SuccessResponse<List<ProductResponse>> getProducts() {

        List<ProductResponse> products = productService.getProducts();

        return new SuccessResponse<>(
                "상품 목록 조회 성공",
                products
        );
    }

    // 상품 등록
    @Operation(
            summary = "상품 등록",
            description = "상품명, 가격, 재고를 입력받아 새로운 상품을 등록합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public SuccessResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {

        ProductResponse data = productService.createProduct(request);

        return new SuccessResponse<>(
                "상품이 정상적으로 등록되었습니다.",
                data
        );
    }
}
// 상품 목록은 단순 조회라 서비스 없이 리포지토리로 해결 가능
// 응답은 DTO로만 반환 (Good practice)