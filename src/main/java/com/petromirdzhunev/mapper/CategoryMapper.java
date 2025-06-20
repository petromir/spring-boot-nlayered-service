package com.petromirdzhunev.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.petromirdzhunev.dto.CategoryDto;
import com.petromirdzhunev.entity.Category;

@Component
public class CategoryMapper {

	public Category toCategory(final CategoryDto categoryDto) {
		if (categoryDto == null) {
			return null;
		}

		return Category.builder()
		               .id(categoryDto.id())
		               .name(categoryDto.name())
		               .description(categoryDto.description())
		               .parentId(categoryDto.parentId())
		               .build();
	}

	public List<CategoryDto> toCategoryDtos(final List<Category> categories) {
		if (categories == null || categories.isEmpty()) {
			return List.of();
		}
		return categories.stream()
		                 .map(this::toCategoryDto)
		                 .collect(Collectors.toList());
	}

	public CategoryDto toCategoryDto(final Category category) {
		if (category == null) {
			return null;
		}

		return new CategoryDto(
				category.getId(),
				category.getName(),
				category.getDescription(),
				category.getParentId()
		);
	}
}