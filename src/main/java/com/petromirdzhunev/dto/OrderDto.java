package com.petromirdzhunev.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(Long id,
                       Long customerId,
                       List<ProductDto> products,
                       BigDecimal totalPrice,
                       String status,
                       LocalDateTime createAt,
		               LocalDateTime updatedAt) {}