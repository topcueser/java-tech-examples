package com.topcueser.springbootrabbitmq.dto;

import java.math.BigDecimal;

public record ResponseProductDto(
    String name,
    BigDecimal price,
    ResponseAttachmentDto attachment
) {
}
