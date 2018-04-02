package com.amruth.airport.baggage.routing.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public enum DelimiterTypes {

    SPACE(" "),
    COLON_DELIMITER(":"),
    COMMA_DELIMITER(","),
    HYPHEN_DELIMITER("-"),
    UNDERSCORE_DELIMITER("_"),
    PATH_DELIMITER("/"),
    NEW_LINE_DELIMITER("\n"),
    LOGGING_DELIMITER("\n\t\t\t");

    private String delimiter;

    DelimiterTypes(String delimiter) {
        this.delimiter = delimiter;
    }

    public static List<String> getDelimiterTypes() {
        return Stream.of(DelimiterTypes.values()).map(DelimiterTypes::getDelimiter).collect(Collectors.toList());
    }

    public String getDelimiter() {
        return delimiter;
    }
}