package com.crewmeister.cmcodingchallenge.api.controllers;

import com.crewmeister.cmcodingchallenge.common.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.common.integration.dto.CurrencyEnum;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.CurrencyResponse;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ForeignExchangeAmount;
import com.crewmeister.cmcodingchallenge.service.currency.CurrencyRatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@RequestMapping("/api/v1/")
@Slf4j
public class CurrencyController {

    @Autowired
    private CurrencyRatesService currencyRatesService;

    @Operation(summary = "Endpoint to get all available currencies.")
    @GetMapping("/currencies")
    public ResponseEntity<CurrencyResponse> getCurrencies() {
            return new ResponseEntity<>(new CurrencyResponse(CurrencyEnum.values()), HttpStatus.OK);

    }

    @Operation(summary = "Endpoint to get Exchange Rates.")
    @GetMapping("/exchange-rate")
    public ResponseEntity<CurrencyConversionRates> getExchageRates(@RequestParam() CurrencyEnum currencyCode,
                                                                @RequestParam(required = false) String date) throws CurrencyNotSupportedException {
        CurrencyConversionRates rates;
        try {
            rates = currencyRatesService.getExchangeRates(currencyCode, date);
        } catch (CurrencyNotSupportedException | IOException e) {
            throw new CurrencyNotSupportedException();
        }
        return ResponseEntity.ok(rates);
    }

    @Operation(summary = "Endpoint to get Foreign Exchange amount.")
    @GetMapping("/exchange-amount")
    public ResponseEntity<ForeignExchangeAmount> getForeignExchangeAmount(@RequestParam() CurrencyEnum currencyCode,
                                                                          @Parameter(description = "Date", example = "2023-01-03")
                                                                          @RequestParam(required = false) String date, @RequestParam() String amount) throws CurrencyNotSupportedException {
        ForeignExchangeAmount rates;
        try {
            rates = currencyRatesService.getForeignExchangeAmount(currencyCode, amount, date);
        } catch (CurrencyNotSupportedException | IOException e) {
            throw new CurrencyNotSupportedException();
        }
        return ResponseEntity.ok(rates);
    }

}
