package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.common.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.common.integration.connector.BundesBankApiConnector;
import com.crewmeister.cmcodingchallenge.common.integration.dto.CurrencyEnum;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ConversionRate;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ForeignExchangeAmount;
import com.crewmeister.cmcodingchallenge.common.integration.external.*;
import com.crewmeister.cmcodingchallenge.common.utils.SchemaTransformUtil;
import com.crewmeister.cmcodingchallenge.common.utils.XMLUtil;
import com.crewmeister.cmcodingchallenge.service.currency.CurrencyRatesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
public class CurrencyRatesServiceTest {

    @Mock
    private BundesBankApiConnector bundesBankApiConnector;

    @InjectMocks
    private CurrencyRatesService currencyRatesService;

    public GenericData createDummyGenericData() throws IOException {
        String resourceName = "test-exchange-data.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String content = Files.readString(file.toPath());
        return XMLUtil.parseData(content, GenericData.class);
    }
    @Test
    public void testGetExchangeRates() throws CurrencyNotSupportedException, IOException {
        CurrencyEnum currencyCode = CurrencyEnum.USD;
        String date = "2023-01-03";

        Mockito.doReturn(createDummyGenericData()).when(bundesBankApiConnector).getExchangeRate(ArgumentMatchers.anyString());
        CurrencyConversionRates rate = currencyRatesService.getExchangeRates(currencyCode, date);
        Assertions.assertEquals(1, rate.getConversionRateList().size());
    }

    @Test
    public void testGetForeignExchangeAmount() throws CurrencyNotSupportedException, IOException {
        CurrencyEnum currencyCode = CurrencyEnum.USD;
        String amount = "100";
        String date = "2022-03-02";

        ConversionRate conversionRate = new ConversionRate("1.4", date, currencyCode);
        List<ConversionRate> conversionRateList = new ArrayList<>();
        conversionRateList.add(conversionRate);
        CurrencyConversionRates currencyConversionRates = new CurrencyConversionRates(new ArrayList<>());
        currencyConversionRates.getConversionRateList().addAll(conversionRateList)
        ;
        CurrencyRatesService spy = Mockito.spy(currencyRatesService);
        doReturn(currencyConversionRates).
                when(spy).getExchangeRates(currencyCode, date);

        // run test
        ForeignExchangeAmount response = spy.getForeignExchangeAmount(currencyCode, amount, date);

        System.out.println(response);
        // assertions
        Assertions.assertNotNull(response);

        Assertions.assertEquals("140.0", response.getAmount());
        Assertions.assertEquals(conversionRate, response.getConversionRate());
    }
}
