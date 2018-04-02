package com.amruth.airport.baggage.routing.domain;

import com.amruth.airport.baggage.routing.models.AirportContext;
import com.amruth.airport.baggage.routing.models.Bag;
import com.amruth.airport.baggage.routing.models.ConveyerSystemEdge;
import com.amruth.airport.baggage.routing.models.Departure;
import com.amruth.airport.baggage.routing.models.GateVertex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class InputMapper {

    private static final Logger logger = LoggerFactory.getLogger(InputMapper.class);

    private InputMapper() {

    }

    public static InputMapper newInstance() {
        return new InputMapper();
    }

    public AirportContext parseInput(String input) {
        AirportContext airportContext = null;
        if (StringUtils.isNotBlank(input)) {
            // split by new line
            List<String> lines = Arrays.asList(input.split("\n"));
            if (!CollectionUtils.isEmpty(lines)) {
                List<String> conveyorSystemsInputLines = lines.subList(1, lines.indexOf("# Section: Departures"));
                List<String> departuresInputLines = lines.subList(lines.indexOf("# Section: Departures") + 1,
                        lines.indexOf("# Section: Bags"));
                List<String> bagsInputLines = lines.subList(lines.indexOf("# Section: Bags") + 1, lines.size());
                Set<String> gateVertices = getGateVertices(conveyorSystemsInputLines);
                logger.info("gateVertices {} ", gateVertices);
                List<GateVertex> gateVertexEntities = createGateVertices(gateVertices);
                List<ConveyerSystemEdge> conveyerSystemEdges = createGateEdges(gateVertexEntities,
                        conveyorSystemsInputLines);
                List<Departure> departures = processDepartures(gateVertexEntities, departuresInputLines);
                List<Bag> bags = processBags(gateVertexEntities, bagsInputLines);
                airportContext = AirportContext.newInstance()
                        .setGateVertices(gateVertexEntities)
                        .setConveyerSystemEdges(conveyerSystemEdges)
                        .setDepartures(departures)
                        .setBags(bags);
            }
        }
        return airportContext;
    }

    private List<Bag> processBags(List<GateVertex> gateVertexEntities, List<String> bagLines) {
        List<Bag> bags = null;
        if (CollectionUtils.isNotEmpty(gateVertexEntities) && CollectionUtils.isNotEmpty(bagLines)) {
            bags = bagLines.stream().filter(Objects::nonNull).map(bagLine -> {
                String[] parts = bagLine.split("\\s+", 3);
                if (parts.length == 3) {
                    Bag bag = new Bag();
                    bag.setBagNumber(parts[0]);
                    bag.setEntryPointGate(getGateVertex(gateVertexEntities, parts[1]));
                    bag.setFlightId(parts[2]);
                    return bag;
                }
                return null;
            }).collect(Collectors.toList());
        }
        return bags;
    }

    private List<Departure> processDepartures(List<GateVertex> gateVertexEntities, List<String> departureLines) {
        List<Departure> departureList = null;
        if (CollectionUtils.isNotEmpty(gateVertexEntities) && CollectionUtils.isNotEmpty(departureLines)) {
            departureList = departureLines.stream().filter(Objects::nonNull).map(line -> {
                String[] parts = line.split("\\s+", 4);
                if (parts.length == 4) {
                    Departure departure = new Departure();
                    departure.setFlightId(parts[0]);
                    departure.setFlightGate(getGateVertex(gateVertexEntities, parts[1]));
                    departure.setDestination(parts[2]);
                    departure.setFlightTime(parts[3]);
                    return departure;
                }
                return null;
            }).collect(Collectors.toList());
        }
        return departureList;
    }

    private List<ConveyerSystemEdge> createGateEdges(List<GateVertex> gateVertexEntities,
                                                     List<String> conveyorSystemsInputLines) {
        final List<ConveyerSystemEdge> conveyerSystemEdges = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(gateVertexEntities) && CollectionUtils.isNotEmpty(conveyorSystemsInputLines)) {
            conveyorSystemsInputLines.stream().filter(Objects::nonNull).forEach(conveyorSystemsInputLine -> {
                String[] parts = conveyorSystemsInputLine.split("\\s+", 3);
                if (parts.length == 3) {
                    ConveyerSystemEdge conveyerSystemEdge = new ConveyerSystemEdge();
                    conveyerSystemEdge.setSourceGateVertex(getGateVertex(gateVertexEntities, parts[0]));
                    conveyerSystemEdge.setDestinationGateVertex(getGateVertex(gateVertexEntities, parts[1]));
                    conveyerSystemEdge.setTravelTime(Integer.parseInt(parts[2]));
                    conveyerSystemEdges.add(conveyerSystemEdge);

                    //For Bi-Directional Edge, adding reverse direction Edge.
                    ConveyerSystemEdge conveyerSystemEdgeBiDirEdge = new ConveyerSystemEdge();
                    conveyerSystemEdgeBiDirEdge.setSourceGateVertex(getGateVertex(gateVertexEntities, parts[1]));
                    conveyerSystemEdgeBiDirEdge.setDestinationGateVertex(getGateVertex(gateVertexEntities, parts[0]));
                    conveyerSystemEdgeBiDirEdge.setTravelTime(Integer.parseInt(parts[2]));
                    conveyerSystemEdges.add(conveyerSystemEdgeBiDirEdge);
                }
            });
        }
        return conveyerSystemEdges;
    }

    private GateVertex getGateVertex(List<GateVertex> gateVertexEntities, String gateName) {
        GateVertex gateVertex = null;
        if (CollectionUtils.isNotEmpty(gateVertexEntities) && StringUtils.isNotBlank(gateName)) {
            gateVertex = gateVertexEntities.stream()
                    .filter(Objects::nonNull)
                    .filter(gateVertexItem -> gateName.equalsIgnoreCase(gateVertexItem.getGateName()))
                    .findAny()
                    .orElse(null);

        }
        return gateVertex;
    }

    private List<GateVertex> createGateVertices(Set<String> gateVertices) {
        List<GateVertex> gateVerticeEntities = null;
        if (CollectionUtils.isNotEmpty(gateVertices)) {
            gateVerticeEntities = gateVertices.stream()
                    .filter(Objects::nonNull)
                    .map(vertex -> new GateVertex(vertex))
                    .collect(Collectors.toList());
        }
        return gateVerticeEntities;
    }

    private Set<String> getGateVertices(List<String> conveyorSystemsInputLines) {
        Set<String> gateVertices = new TreeSet<>();
        conveyorSystemsInputLines.stream().filter(Objects::nonNull).forEach(line -> {
            String[] parts = line.split("\\s+", 3);
            if (parts.length == 3) {
                gateVertices.add(parts[0]);
                gateVertices.add(parts[1]);
            }
        });
        return gateVertices;
    }
}
