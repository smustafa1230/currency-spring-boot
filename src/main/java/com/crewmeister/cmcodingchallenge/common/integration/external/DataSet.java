package com.crewmeister.cmcodingchallenge.common.integration.external;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DataSet {
    private String structureRef;
    private String setID;
    private String action;
    private String validFromDate;

    @JacksonXmlProperty(localName = "Series")
    Series series;
}
