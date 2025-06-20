package com.petromirdzhunev.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.petromirdzhunev.entity.Category;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

	public Category createCategory(final Category category) {
		return null;
	}

	public Category updateCategory(final Long id, final Category category) {
		return null;
	}

	public void deleteCategory(final Long id) {
	}

	public List<Category> categories() {
		return null;
	}

	public Category category(final Long id) {
		return null;
	}
}