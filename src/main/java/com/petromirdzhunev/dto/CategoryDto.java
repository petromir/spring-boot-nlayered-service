package com.petromirdzhunev.dto;

public record CategoryDto(Long id,
                          String name,
                          String description,
                          Long parentId) {}