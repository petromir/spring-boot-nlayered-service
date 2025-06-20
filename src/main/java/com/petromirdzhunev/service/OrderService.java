package com.petromirdzhunev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petromirdzhunev.dto.OrderDto;
import com.petromirdzhunev.entity.Order;
import com.petromirdzhunev.mapper.OrderMapper;
import com.petromirdzhunev.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	public OrderDto createOrder(final OrderDto orderDto) {
		final Order order = orderMapper.toOrder(orderDto);
		return orderMapper.toOrderDto(orderRepository.createOrder(order));
	}

	public List<OrderDto> orders(final Long customerId) {
		return orderMapper.toOrderDtos(orderRepository.orders(customerId));
	}

	public OrderDto order(final Long id) {
		return orderMapper.toOrderDto(orderRepository.order(id));
	}

	public OrderDto updateOrder(final Long id, final OrderDto orderDto) {
		return orderMapper.toOrderDto(orderRepository.updateOrder(id, orderMapper.toOrder(orderDto)));
	}

	public void deleteOrder(final Long id) {
		orderRepository.deleteOrder(id);
	}
}