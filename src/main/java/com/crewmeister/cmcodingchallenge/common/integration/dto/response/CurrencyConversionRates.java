package com.crewmeister.cmcodingchallenge.common.integration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionRates {
    private List<ConversionRate> conversionRateList;
}
