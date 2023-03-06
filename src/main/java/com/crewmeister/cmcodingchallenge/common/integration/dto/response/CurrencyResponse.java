package com.crewmeister.cmcodingchallenge.common.integration.dto.response;

import com.crewmeister.cmcodingchallenge.common.integration.dto.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyResponse {
    private CurrencyEnum[] currencies;
}
