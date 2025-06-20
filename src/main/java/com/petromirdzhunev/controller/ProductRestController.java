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

import com.petromirdzhunev.dto.ProductDto;
import com.petromirdzhunev.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/inventory")
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;

	@GetMapping("/products")
	public List<ProductDto> products(@RequestParam Long categoryId) {
		return productService.products(categoryId);
	}

	@GetMapping("/products/{id}")
	public ProductDto product(@PathVariable Long id) {
		return productService.product(id);
	}

	@PostMapping("/products")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDto createProduct(@RequestBody ProductDto productDto) {
		return productService.createProduct(productDto);
	}

	@PutMapping("/products/{id}")
	public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
		return productService.updateProduct(id, productDto);
	}

	@DeleteMapping("/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}