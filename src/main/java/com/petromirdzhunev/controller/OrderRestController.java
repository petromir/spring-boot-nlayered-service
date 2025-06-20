package com.petromirdzhunev.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petromirdzhunev.dto.OrderDto;
import com.petromirdzhunev.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderRestController {

	private final OrderService orderService;

	@GetMapping("/products")
	public List<OrderDto> orders(@RequestParam Long customerId) {
		return orderService.orders(customerId);
	}

	@GetMapping("/products/{id}")
	public OrderDto order(@PathVariable Long id) {
		return orderService.order(id);
	}

	@PostMapping("/products")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDto createOrder(@RequestBody OrderDto orderDto) {
		return orderService.createOrder(orderDto);
	}

	@PutMapping("/products/{id}")
	public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
		return orderService.updateOrder(id, orderDto);
	}

	@DeleteMapping("/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
	}
}