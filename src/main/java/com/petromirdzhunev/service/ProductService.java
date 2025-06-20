package com.petromirdzhunev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petromirdzhunev.dto.ProductDto;
import com.petromirdzhunev.entity.Product;
import com.petromirdzhunev.mapper.ProductMapper;
import com.petromirdzhunev.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public ProductDto createProduct(final ProductDto productDto) {
		final Product product = productMapper.toProduct(productDto);
		return productMapper.toProductDto(productRepository.createProduct(product));
	}

	public List<ProductDto> products(final Long categoryId) {
		return productMapper.toProductDtos(productRepository.products(categoryId));
	}

	public ProductDto product(final Long id) {
		return productMapper.toProductDto(productRepository.product(id));
	}

	public ProductDto updateProduct(final Long id, final ProductDto productDto) {
		return productMapper.toProductDto(productRepository.updateProduct(id, productMapper.toProduct(productDto)));
	}

	public void deleteProduct(final Long id) {
		productRepository.deleteProduct(id);
	}
}