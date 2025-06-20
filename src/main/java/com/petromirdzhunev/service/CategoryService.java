package com.petromirdzhunev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petromirdzhunev.dto.CategoryDto;
import com.petromirdzhunev.entity.Category;
import com.petromirdzhunev.mapper.CategoryMapper;
import com.petromirdzhunev.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryDto createCategory(final CategoryDto categoryDto) {
		final Category category = categoryMapper.toCategory(categoryDto);
		return categoryMapper.toCategoryDto(categoryRepository.createCategory(category));
	}

	public List<CategoryDto> categories() {
		return categoryMapper.toCategoryDtos(categoryRepository.categories());
	}

	public CategoryDto category(final Long id) {
		return categoryMapper.toCategoryDto(categoryRepository.category(id));
	}

	public CategoryDto updateCategory(final Long id, final CategoryDto categoryDto) {
		return categoryMapper.toCategoryDto(categoryRepository.updateCategory(id,
				categoryMapper.toCategory(categoryDto)));
	}

	public void deleteCategory(final Long id) {
		categoryRepository.deleteCategory(id);
	}
}