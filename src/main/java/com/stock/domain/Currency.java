package com.stock.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency implements Serializable {


    @NotNull
    @Range(min = 0)
    private BigDecimal amount;

    @JsonProperty("currency")
    private CurrencyCode currencyCode = CurrencyCode.USD;

}
