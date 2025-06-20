package com.petromirdzhunev.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.petromirdzhunev.entity.Product;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
	// TODO: Implement logic using jOOQ entities

	public Product createProduct(final Product product) {
		return null;
	}

	public Product updateProduct(final Long id, final Product product) {
		return null;
	}

	public void deleteProduct(final Long id) {

	}

	public List<Product> products(final Long categoryId) {
		return null;
	}

	public Product product(final Long id) {
		return null;
	}
}