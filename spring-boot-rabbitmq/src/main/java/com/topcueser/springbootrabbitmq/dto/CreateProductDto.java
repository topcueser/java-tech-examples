package com.topcueser.springbootrabbitmq.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductDto (
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, message = "Name must be min 3 characters")
    String name,

    @NotNull(message = "Price cannot be null")
    BigDecimal price
){
}
