package com.crewmeister.cmcodingchallenge.common.integration.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Obs {
    @JacksonXmlProperty(localName = "ObsDimension")
    private ObsDimension obsDimension;

    @JacksonXmlProperty(localName = "ObsValue")
    private ObsValue obsValue;
}
