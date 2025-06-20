package com.petromirdzhunev.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petromirdzhunev.dto.OrderDto;
import com.petromirdzhunev.entity.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

	private final ProductMapper productMapper;

	public Order toOrder(final OrderDto orderDto) {
		if (orderDto == null) {
			return null;
		}

		return Order.builder()
		            .id(orderDto.id())
		            .customerId(orderDto.customerId())
		            .products(productMapper.toProducts(orderDto.products()))
		            .totalPrice(orderDto.totalPrice())
		            .status(orderDto.status())
		            .createAt(orderDto.createAt())
		            .updatedAt(orderDto.updatedAt())
		            .build();
	}

	public List<OrderDto> toOrderDtos(final List<Order> orders) {
		if (orders == null || orders.isEmpty()) {
			return List.of();
		}
		return orders.stream()
		             .map(this::toOrderDto)
		             .collect(Collectors.toList());
	}

	public OrderDto toOrderDto(final Order order) {
		if (order == null) {
			return null;
		}

		return new OrderDto(
				order.getId(),
				order.getCustomerId(),
				productMapper.toProductDtos(order.getProducts()),
				order.getTotalPrice(),
				order.getStatus(),
				order.getCreateAt(),
				order.getUpdatedAt()
		);
	}
}