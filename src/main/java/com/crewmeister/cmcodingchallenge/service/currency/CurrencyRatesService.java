package com.crewmeister.cmcodingchallenge.service.currency;

import com.crewmeister.cmcodingchallenge.common.exception.CurrencyNotSupportedException;
import com.crewmeister.cmcodingchallenge.common.integration.connector.BundesBankApiConnector;
import com.crewmeister.cmcodingchallenge.common.integration.dto.CurrencyEnum;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ConversionRate;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ForeignExchangeAmount;
import com.crewmeister.cmcodingchallenge.common.integration.external.GenericData;
import com.crewmeister.cmcodingchallenge.common.utils.SchemaTransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
public class CurrencyRatesService {

    @Value("${BUNDESBANKAPI_URL}")
    private String BUNDESBANKAPI_URL;

    private static final String TIME_SERIES = "/rest/data/BBEX3/D.XXX.EUR.BB.AC.000";
    private final String currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    @Autowired
    private BundesBankApiConnector bundesBankApiConnector;

    /**
     * Get Exchange Rates
     * @param currencyCode
     * @param date
     * @return
     * @throws CurrencyNotSupportedException
     * @throws IOException
     */
    public CurrencyConversionRates getExchangeRates(CurrencyEnum currencyCode, String date) throws CurrencyNotSupportedException, IOException {
        UriComponentsBuilder uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host(BUNDESBANKAPI_URL)
                .path(TIME_SERIES.replace("XXX", currencyCode.toString()))
                .queryParam("detail", "dataonly");

        if (date != null) {
            // can get different end period as well
            uriComponents.queryParam("startPeriod", date)
                    .queryParam("endPeriod", date);
        }
        GenericData resp = bundesBankApiConnector.getExchangeRate(uriComponents.build().toString());
        return SchemaTransformUtil.transform(resp);
    }

    /**
     * get Foreign Exchange Amount
     * @param currencyCode
     * @param amount
     * @param date
     * @return
     * @throws CurrencyNotSupportedException
     * @throws IOException
     */
    public ForeignExchangeAmount getForeignExchangeAmount(CurrencyEnum currencyCode, String amount, String date)
            throws CurrencyNotSupportedException, IOException {
        Optional<ConversionRate> currencyConversionRate =
                getExchangeRates(currencyCode, date == null ? currentDate : date).getConversionRateList().stream().findFirst();
        log.info("Currency conversion Rate: {}", currencyConversionRate);
        ForeignExchangeAmount exchangedAmount = new ForeignExchangeAmount();
        if (currencyConversionRate.isPresent()) {
            try {
                double conversionRate = Double.parseDouble(currencyConversionRate.get().getRate());
                exchangedAmount.setConversionRate(currencyConversionRate.get());
                exchangedAmount.setAmount(String.valueOf(conversionRate * Double.parseDouble(amount)));
            } catch (NumberFormatException nfe) {
                log.error("action=getForeignExchangeAmount, error: {}", nfe.getMessage());
                throw new CurrencyNotSupportedException();
            }
        }
        return exchangedAmount;
    }
}
