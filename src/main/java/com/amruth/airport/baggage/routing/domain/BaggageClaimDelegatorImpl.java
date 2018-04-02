package com.amruth.airport.baggage.routing.domain;

import com.amruth.airport.baggage.routing.models.AirportContext;
import com.amruth.airport.baggage.routing.models.Bag;
import com.amruth.airport.baggage.routing.models.ConveyerSystemEdge;
import com.amruth.airport.baggage.routing.models.DelimiterTypes;
import com.amruth.airport.baggage.routing.models.Departure;
import com.amruth.airport.baggage.routing.service.DijkstraAlgorithmService;
import com.amruth.airport.baggage.routing.service.DijkstraAlgorithmServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class BaggageClaimDelegatorImpl {

    private static final Logger logger = LoggerFactory.getLogger(BaggageClaimDelegatorImpl.class);
    private static final String FLIGHT_STATUS_ARRIVAL = "ARRIVAL";
    private static final String DESTINATION_BAGGAGE_CLAIM = "BaggageClaim";
    private DijkstraAlgorithmService dijkstraAlgorithmService = DijkstraAlgorithmServiceImpl.newInstance();
    private InputMapper inputMapper = InputMapper.newInstance();

    private BaggageClaimDelegatorImpl() {

    }

    public static BaggageClaimDelegatorImpl newInstance() {
        return new BaggageClaimDelegatorImpl();
    }

    public String findBaggageRoutingPaths(String fileName) {
        String shortestPath = null;
        try {
            String inputFile = readInputFile(fileName);
            AirportContext airportContext = getAirportContext(inputFile);
            shortestPath = process(airportContext);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return shortestPath;
    }

    private String process(AirportContext airportContext) {
        String shortestPath = null;
        if (null != airportContext && CollectionUtils.isNotEmpty(airportContext.getBags())) {
            // for each bag find shortest path
            List<String> shortestPaths = airportContext.getBags()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(bag -> findBaggageRoutingPaths(airportContext, bag))
                    .collect(Collectors.toList());
            // Order by Bag number
            Collections.sort(shortestPaths);
            shortestPath = shortestPaths.stream()
                    .collect(Collectors.joining(DelimiterTypes.NEW_LINE_DELIMITER.getDelimiter()));
        }
        return shortestPath;
    }

    private String findBaggageRoutingPaths(AirportContext airportContext, Bag bag) {
        String entryPointGate = bag.getEntryPointGate().getGateName();
        String destination = findDestination(airportContext, bag.getFlightId());
        String bagNumber = bag.getBagNumber();
        String shortestPath = findBaggageRoutingPaths(entryPointGate, destination,
                airportContext.getConveyerSystemEdges());
        return new StringBuilder().append(bagNumber)
                .append(DelimiterTypes.SPACE.getDelimiter())
                .append(shortestPath)
                .toString();
    }

    private String findBaggageRoutingPaths(String entryPointGate, String destination,
                                           List<ConveyerSystemEdge> conveyerSystemEdges) {
        return dijkstraAlgorithmService.findShortestPath(entryPointGate, destination, conveyerSystemEdges);
    }

    private String findDestination(AirportContext airportContext, String flightId) {
        String destination;
        if (FLIGHT_STATUS_ARRIVAL.equalsIgnoreCase(flightId)) {
            destination = DESTINATION_BAGGAGE_CLAIM;
        } else {
            destination = getDepartureFlight(airportContext.getDepartures(), flightId).getFlightGate().getGateName();
        }
        return destination;
    }

    private Departure getDepartureFlight(List<Departure> departures, String flightId) {
        Departure departure = null;
        if (CollectionUtils.isNotEmpty(departures) && StringUtils.isNotBlank(flightId)) {
            departure = departures.stream()
                    .filter(Objects::nonNull)
                    .filter(departureItem -> flightId.equalsIgnoreCase(departureItem.getFlightId()))
                    .findAny()
                    .orElse(null);
        }
        return departure;
    }

    private AirportContext getAirportContext(String inputFile) {
        AirportContext airportContext = inputMapper.parseInput(inputFile);
        return airportContext;
    }

    private String readInputFile(String fileName) throws FileNotFoundException {
        String inputFile;
        try {
            inputFile = readFileAsString(fileName);
            logger.info("Input File : {}", inputFile);
        } catch (IOException | URISyntaxException e) {
            String message = MessageFormat.format("Failed to Retrieve File : {0}", fileName);
            logger.error(message);
            throw new FileNotFoundException(message);
        }
        return inputFile;
    }

    private String readFileAsString(String fileName) throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        return null != resource ? new String(Files.readAllBytes(Paths.get(resource.toURI()))) : null;
    }
}
