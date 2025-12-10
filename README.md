# 🛒 주문 & 상품 관리 REST API 프로젝트 (Order & Product API)

Spring Boot 기반으로 구현된 **주문 / 상품 관리 REST API** 프로젝트입니다.  
상품 등록, 주문 생성·조회·취소 기능과 함께 **공통 성공 응답 구조**,  
**전역 예외 처리(GlobalExceptionHandler)**, **Swagger 문서화**, **MySQL + JPA 기반 데이터 처리** 등을 포함합니다.

# ✨ 설계 시 고민했던 부분

## 1) 응답 구조 통일 필요성
REST API를 구축할 때 엔드포인트마다 서로 다른 응답 형식을 반환하면  
클라이언트가 응답을 파싱하기 어렵고 유지보수성이 떨어짐.
그래서 모든 API가 아래 두 가지 형태 중 하나만 반환하도록 설계함:

- 성공 → `SuccessResponse<T>`
- 실패 → `ApiErrorResponse`

이를 통해 클라이언트는 항상 같은 구조로 응답을 처리 가능해짐.

## 2) 전역 예외 처리 도입 이유
초기 개발 시 각 컨트롤러마다 try/catch를 작성하는 방식은 불필요한 중복이 많고,  
새로운 예외가 생길 때마다 모든 컨트롤러를 수정해야 했음.

그래서 `@ControllerAdvice` 기반의 **GlobalExceptionHandler**를 두어  
아래와 같은 예외를 한 곳에서 처리하도록 구조화함:

- InvalidInputException
- InsufficientStockException
- OrderNotFoundException
- NoOrderFoundException
- AlreadyCanceledException
- 기타 서버 내부 오류

## 3) 주문 생성 시 재고 처리 책임의 위치
재고 감소와 복구 기능을 어디서 처리할 것인지가 핵심 고민이었음:

### ❌ OrderService에서 직접 재고 감소 처리  
→ 서비스가 불필요하게 많은 책임을 가짐  
→ 객체지향적이지 않음

### ✔️ Product 엔티티가 재고 감소/복구 기능을 소유하도록 설계  
→ 도메인 객체가 자신의 상태를 스스로 관리  
→ 수정 시 영향을 최소화할 수 있어 유지보수에 유리

## 4) 주문 취소 시 재고 복구 설계
중복 취소(이미 취소된 주문을 다시 취소)하는 문제가 발생할 여지가 있어  
`AlreadyCanceledException` 을 추가하여 이를 방지.

또한 상태 변경과 재고 복구는 Order 엔티티 내부에서 처리되도록 구현하여  
서비스 계층의 코드 복잡도를 줄임.

## 5) DTO 설계 고민
엔티티를 그대로 반환하면:

- 순환 참조 위험 (Order ↔ OrderItem)
- 민감 정보 노출 가능
- 불필요한 필드 포함

따라서 필요한 필드만 담는 DTO를 별도로 설계:  
`OrderResponse`, `OrderItemResponse`, `ProductResponse` 등.

## 6) Swagger 적용 버전 충돌 해결
Boot 3.5.x 환경과 springdoc-openapi 2.5.0은 충돌이 발생하여  
Swagger UI가 열리지 않는 문제가 있었음.

해결 방법:
- **Spring Boot 버전을 3.2.x로 다운그레이드**
- springdoc-openapi 2.5.0 유지

이 조합이 안정적으로 작동하여 Swagger 문서화가 정상 구현됨.

# 🛠 1. 기술 스택

| 구분 | 내용 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.x |
| DB | MySQL 8.x |
| ORM | Spring Data JPA |
| Build Tool | Maven |
| API 문서화 | Swagger (springdoc-openapi) |
| Validation | Jakarta Validation |

---

# 📦 2. 프로젝트 구조

```
src/main/java/com/example/demo
├── controller
│   ├── OrderController.java
│   └── ProductController.java
├── dto
│   ├── OrderCreateRequest.java
│   ├── OrderCreateResponse.java
│   ├── OrderResponse.java
│   ├── OrderItemResponse.java
│   ├── ProductCreateRequest.java
│   ├── ProductCreateResponse.java
│   ├── ProductResponse.java
│   └── SuccessResponse.java
├── entity
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Product.java
│   └── OrderStatus.java
├── exception
│   ├── ApiErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── AlreadyCanceledException.java
│   ├── InvalidInputException.java
│   ├── OrderNotFoundException.java
│   ├── NoOrderFoundException.java
│   └── InsufficientStockException.java
├── repository
│   ├── OrderRepository.java
│   ├── ProductRepository.java
│   └── OrderItemRepository.java
└── service
    ├── OrderService.java
    └── ProductService.java
```

# 📝 3. API 명세

## ✔️ 상품 등록 (POST /products)

### Request
```json
{
  "name": "사과",
  "price": 1200,
  "stock": 10
}
```

### Response
```json
{
  "success": true,
  "message": "상품이 정상적으로 등록되었습니다.",
  "data": {
    "name": "사과",
    "price": 1200,
    "stock": 10
  }
}
```

## ✔️ 상품 목록 조회 (GET /products)

### Response
```json
{
  "success": true,
  "message": "상품 목록 조회 성공",
  "data": [
    {
      "name": "사과",
      "price": 1200,
      "stock": 10
    }
  ]
}
```

## ✔️ 주문 생성 (POST /orders)

### Request
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

### Response
```json
{
  "success": true,
  "message": "주문이 정상적으로 진행되었습니다.",
  "data": { "orderId": 10 }
}
```

## ✔️ 주문 목록 조회 (GET /orders?userId=1)

## ✔️ 주문 상세 조회 (GET /orders/{orderId})

## ✔️ 주문 취소 (DELETE /orders/{orderId})

# ⚠️ 4. 예외 처리 구조

예외는 전부 `GlobalExceptionHandler` 에서 잡혀 JSON 형태로 반환됨.

### 에러 응답 예시
```json
{
  "errorCode": "NO_ORDER_FOUND",
  "message": "해당 사용자의 주문이 존재하지 않습니다.",
  "status": 404,
  "timestamp": "2025-01-01T12:00:00"
}
```

# 🗄 5. ERD 구조

```
Product (1) —— (N) OrderItem (N) —— (1) Order
```

# ⚙️ 6. 실행 방법

## 1) MySQL DB 생성
```sql
CREATE DATABASE order_db;
```

## 2) application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/order_db
spring.datasource.username=root
spring.datasource.password=비밀번호
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 3) 서버 실행
```
mvn spring-boot:run
```

## 4) Swagger 접속  
👉 http://localhost:8080/swagger-ui/index.html
