package com.petromirdzhunev.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.petromirdzhunev.entity.Order;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	// TODO: Implement logic using jOOQ entities
	public Order createOrder(final Order product) {
		return null;
	}

	public Order updateOrder(final Long id, final Order product) {
		return null;
	}

	public void deleteOrder(final Long id) {
	}

	public List<Order> orders(final Long customerId) {
		return null;
	}

	public Order order(final Long id) {
		return null;
	}
}