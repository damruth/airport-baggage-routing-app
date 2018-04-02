package com.amruth.airport.baggage.routing;

import com.amruth.airport.baggage.routing.domain.BaggageClaimDelegatorImpl;
import com.amruth.airport.baggage.routing.models.DelimiterTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirportBaggageRoutingApp {
    private static final Logger logger = LoggerFactory.getLogger(AirportBaggageRoutingApp.class);

    public static void main(String[] args) {
        AirportBaggageRoutingApp  airportBaggageRoutingApp = new AirportBaggageRoutingApp();
        airportBaggageRoutingApp.execute();
    }

    private void execute() {
        String baggageRoutingPaths = BaggageClaimDelegatorImpl.newInstance().findBaggageRoutingPaths("inputDataFile.txt");
        logger.info("Denver Airport Baggage Routing Shortest Path : {}", StringUtils.isNotBlank(baggageRoutingPaths) ? DelimiterTypes.NEW_LINE_DELIMITER.getDelimiter().concat(baggageRoutingPaths) : "Not Found.");
    }
}
