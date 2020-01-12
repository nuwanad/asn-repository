package com.asn.app.product.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateDTO {

    @Size(max = 50, message = "Name max length exceeded")
    @NotBlank(message = "Name cannot be null or blank")
    private String name;

    @NotNull(message = "CurrentPrice cannot be null")
    @DecimalMin(value = "0.00", message = "price cannot be zero or negative", inclusive = false)
    private BigDecimal currentPrice;

}
