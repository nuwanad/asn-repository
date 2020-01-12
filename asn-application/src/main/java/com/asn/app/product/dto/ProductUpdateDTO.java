package com.asn.app.product.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "currentPrice", "description"})
public class ProductUpdateDTO {

    @Size(max = 50, message = "Name max length exceeded")
    @NotBlank(message = "Name cannot be null or blank")
    private String name;

    @NotNull(message = "CurrentPrice cannot be null")
    @DecimalMin(value = "0.00", message = "price cannot be zero or negative", inclusive = false)
    private BigDecimal currentPrice;

    @Size(max = 100, message = "Description max length exceeded")
    @NotNull(message = "Description cannot be null")
    private String description;
}
