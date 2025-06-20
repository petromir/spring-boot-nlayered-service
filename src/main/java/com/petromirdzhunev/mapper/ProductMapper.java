package com.petromirdzhunev.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.petromirdzhunev.dto.ProductDto;
import com.petromirdzhunev.entity.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

	private final CategoryMapper categoryMapper;

	public Product toProduct(final ProductDto productDto) {
		if (productDto == null) {
			return null;
		}

		return Product.builder()
		              .id(productDto.id())
		              .name(productDto.name())
		              .description(productDto.description())
		              .price(productDto.price())
		              .stock(productDto.stock())
		              .image(productDto.image())
		              .category(categoryMapper.toCategory(productDto.category()))
		              .build();
	}

	public List<Product> toProducts(final List<ProductDto> productDTOs) {
		if (productDTOs == null || productDTOs.isEmpty()) {
			return List.of();
		}
		return productDTOs.stream()
		                  .map(this::toProduct)
		                  .toList();
	}

	public List<ProductDto> toProductDtos(final List<Product> products) {
		if (products == null || products.isEmpty()) {
			return List.of();
		}
		return products.stream()
		               .map(this::toProductDto)
		               .toList();
	}

	public ProductDto toProductDto(final Product product) {
		if (product == null) {
			return null;
		}

		return new ProductDto(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getStock(),
				product.getImage(),
				categoryMapper.toCategoryDto(product.getCategory())
		);
	}
}