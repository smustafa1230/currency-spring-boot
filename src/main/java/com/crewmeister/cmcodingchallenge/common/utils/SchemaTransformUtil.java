package com.crewmeister.cmcodingchallenge.common.utils;

import com.crewmeister.cmcodingchallenge.common.integration.dto.CurrencyEnum;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.ConversionRate;
import com.crewmeister.cmcodingchallenge.common.integration.dto.response.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.common.integration.external.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemaTransformUtil {
        private static final String BBK_STD_CURRENCY = "BBK_STD_CURRENCY";

        private SchemaTransformUtil() {
            throw new IllegalStateException("utility class");
        }
        public static CurrencyConversionRates transform(GenericData genericData) {
            CurrencyConversionRates currencyConversionRates = new CurrencyConversionRates(new ArrayList<>());
            Series series = extractSeries(genericData);
            List<Obs> obsList = extractObsList(series);
            if (!obsList.isEmpty()) {
                currencyConversionRates.getConversionRateList().addAll(
                        obsList.stream().map(e -> constructObject(series, e)).collect(Collectors.toList()));
            }
            return currencyConversionRates;
        }

        private static Series extractSeries(GenericData genericData) {
            Optional<DataSet> dataset = Optional
                    .ofNullable(genericData)
                    .map(o -> Optional.ofNullable(o.getDataSet()).orElse(DataSet.builder().build()));

            Optional<Series> series = Optional.empty();
            if (dataset.isPresent()) {
                series = dataset.map(o -> Optional.ofNullable(o.getSeries())).get();
            }
//            series = dataset.map(item -> Optional.ofNullable(item.getSeries()).get());
            return series.orElse(Series.builder().build());
        }

        private static String extractSeriesValue(List<Value> valueList, String key) {
            return valueList.stream().filter(v -> key.equals(v.getId())).findFirst().orElse(new Value()).getValue();
        }

        private static List<Obs> extractObsList(Series series) {
            return Optional.ofNullable(series)
                    .map(o -> Optional.ofNullable(o.getObsList()).orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());
        }

        private static ConversionRate constructObject(Series series, Obs e) {
            return new ConversionRate(
                    Optional.ofNullable(e.getObsValue()).orElse(new ObsValue("No value available")).getValue(),
                    Optional.ofNullable(e.getObsDimension()).orElse(new ObsDimension()).getValue(),
                    CurrencyEnum.valueOf(extractSeriesValue(series.getSeriesKey().getValueList(), BBK_STD_CURRENCY)));
        }

}
