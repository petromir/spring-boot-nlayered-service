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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.petromirdzhunev.dto.CategoryDto;
import com.petromirdzhunev.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/inventory")
@RequiredArgsConstructor
public class CategoryRestController {

	private final CategoryService categoryService;

	@GetMapping("/categories")
	public List<CategoryDto> categories() {
		return categoryService.categories();
	}

	@GetMapping("/categories/{id}")
	public CategoryDto category(@PathVariable Long id) {
		return categoryService.category(id);
	}

	@PostMapping("/categories")
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
		return categoryService.createCategory(categoryDto);
	}

	@PutMapping("/categories/{id}")
	public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
		return categoryService.updateCategory(id, categoryDto);
	}

	@DeleteMapping("/categories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Long id) {
		categoryService.deleteCategory(id);
	}
}