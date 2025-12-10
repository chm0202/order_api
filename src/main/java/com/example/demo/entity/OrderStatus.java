package com.example.demo.entity;

public enum OrderStatus {
	CREATED,
	CANCELED
}
// 주문 상태를 문자열로 DB에 저장하기 위해 Enum 사용
// 추후 확장(배송, 완료 등)도 쉽게 가능함
