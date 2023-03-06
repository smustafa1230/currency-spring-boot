package com.crewmeister.cmcodingchallenge.common.integration.connector;

import com.crewmeister.cmcodingchallenge.common.integration.external.GenericData;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Log4j2
public class BundesBankApiConnector {

    private final RestTemplate restTemplate;

    public BundesBankApiConnector( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GenericData getExchangeRate(String url)
            throws IOException {

        return restTemplate.getForObject(
                url,
                GenericData.class
        );
    }
}
