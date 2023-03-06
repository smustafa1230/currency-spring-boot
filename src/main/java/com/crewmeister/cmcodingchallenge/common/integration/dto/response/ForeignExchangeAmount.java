package com.crewmeister.cmcodingchallenge.common.integration.dto.response;

import lombok.Data;

@Data
public class ForeignExchangeAmount {
    private String amount;
    private ConversionRate conversionRate;
}
