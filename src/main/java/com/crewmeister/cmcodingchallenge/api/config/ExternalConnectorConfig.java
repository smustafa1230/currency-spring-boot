package com.crewmeister.cmcodingchallenge.api.config;

import com.crewmeister.cmcodingchallenge.common.integration.connector.BundesBankApiConnector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ExternalConnectorConfig {

    public static final Duration STANDARD_TIME_OUT = Duration.ofSeconds(5L);
    public static final Duration LONG_10S_TIME_OUT = Duration.ofSeconds(10L);
    public static final Duration LONG_20S_TIME_OUT = Duration.ofSeconds(20L);
    public static final Duration LONG_30S_TIME_OUT = Duration.ofSeconds(30L);

    private RestTemplate buildRestTemplate(Duration duration) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(duration)
                .setReadTimeout(duration)
                .requestFactory(() -> new BufferingClientHttpRequestFactory(
                        new SimpleClientHttpRequestFactory()))
                .build();

        ObjectMapper objectMapper = restTemplate.getMessageConverters()
                .stream()
                .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                .map(converter -> (MappingJackson2HttpMessageConverter) converter)
                .map(AbstractJackson2HttpMessageConverter::getObjectMapper)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find object mapper"));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateStandard() {
        return buildRestTemplate(STANDARD_TIME_OUT);
    }

    @Bean
    public RestTemplate restTemplate10() {
        return buildRestTemplate(LONG_10S_TIME_OUT);
    }

    @Bean
    public BundesBankApiConnector bundesBankApiConnector() {
        return new BundesBankApiConnector( restTemplateStandard());
    }
}
