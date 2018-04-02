package com.amruth.airport.baggage.routing.service;

import com.amruth.airport.baggage.routing.models.ConveyerSystemEdge;
import com.amruth.airport.baggage.routing.models.DelimiterTypes;
import com.amruth.airport.baggage.routing.models.GateVertex;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class DijkstraAlgorithmServiceImpl implements DijkstraAlgorithmService {

    private DijkstraAlgorithmServiceImpl() {

    }

    public static DijkstraAlgorithmServiceImpl newInstance() {
        return new DijkstraAlgorithmServiceImpl();
    }

    @Override
    public String findShortestPath(String originGate, String destGate, List<ConveyerSystemEdge> edges) {
        //Populate Graph from given  ConveyerSystemEdges
        Map<String, GateVertex> graph = populateGraph(edges);
        NavigableSet<GateVertex> vertexNavigableSet = initializeGateVerticesWithDefaultValues(originGate, graph);
        findDijkstraShortestPath(vertexNavigableSet);
        List<GateVertex> shortestPath = extractShortestPathToGivenVertex(destGate, graph);
        return constructTravelRoute(shortestPath);
    }

    private List<GateVertex> extractShortestPathToGivenVertex(String destinationGateName,
                                                              Map<String, GateVertex> graph) {
        if (!graph.containsKey(destinationGateName)) {
            throw new DijkstraException(
                    "Given Destination Gate Vertex does not exist in Graph : " + destinationGateName);
        }
        List<GateVertex> travelRoute = new ArrayList();
        GateVertex gateVertex = graph.get(destinationGateName);
        do {
            travelRoute.add(gateVertex);
            gateVertex = gateVertex.getPreviousVertex();
        } while (gateVertex != null && !travelRoute.contains(gateVertex));
        Collections.reverse(travelRoute);
        return travelRoute;
    }

    private NavigableSet<GateVertex> initializeGateVerticesWithDefaultValues(String originGateName,
                                                                             Map<String, GateVertex> graph) {
        if (!graph.containsKey(originGateName)) {
            String message = MessageFormat.format("Given Origin Gate Vertex does not exist in Graph : {0}",
                    originGateName);
            throw new DijkstraException(message);
        }
        final GateVertex originGateVertex = graph.get(originGateName);
        // Initialize the Gate Vertices with Max Values.
        return graph.values().stream().filter(Objects::nonNull).map(gateVertex -> {
            gateVertex.setPreviousVertex(gateVertex == originGateVertex ? originGateVertex : null);
            gateVertex.setTravelTime(gateVertex == originGateVertex ? 0 : Integer.MAX_VALUE);
            return gateVertex;
        }).collect(Collectors.toCollection(TreeSet::new));
    }

    private void findDijkstraShortestPath(final NavigableSet<GateVertex> gateVertices) {
        // Find the Shortest Path using Dijkstra's algorithm.
        while (!gateVertices.isEmpty()) {
            GateVertex sourceGateVertex = gateVertices.pollFirst();
            if (sourceGateVertex.getTravelTime() == Integer.MAX_VALUE)
                break;
            sourceGateVertex.getNeighbours()
                    .entrySet()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(neighbourGateVertexEntry -> {
                        GateVertex neighbourGateVertex = neighbourGateVertexEntry.getKey();
                        final int cumulativeTravelTime = sourceGateVertex.getTravelTime()
                                + neighbourGateVertexEntry.getValue();
                        if (cumulativeTravelTime < neighbourGateVertex.getTravelTime()) {
                            neighbourGateVertex.setTravelTime(cumulativeTravelTime);
                            neighbourGateVertex.setPreviousVertex(sourceGateVertex);
                            gateVertices.add(neighbourGateVertex);
                        }
                    });
        }
    }

    private Map<String, GateVertex> populateGraph(List<ConveyerSystemEdge> conveyerSystemEdges) {
        Map<String, GateVertex> graph = new HashMap<>(conveyerSystemEdges.size());
        //Populate all the Vertices from the Conveyer System Edges
        conveyerSystemEdges.stream().filter(Objects::nonNull).forEach(conveyerSystemEdge -> {
            graph.putIfAbsent(conveyerSystemEdge.getSourceGateVertex().getGateName(),
                    conveyerSystemEdge.getSourceGateVertex());
            graph.putIfAbsent(conveyerSystemEdge.getDestinationGateVertex().getGateName(),
                    conveyerSystemEdge.getDestinationGateVertex());
        });

        //Populate all the neighbouring Vertices
        conveyerSystemEdges.stream()
                .filter(Objects::nonNull)
                .forEach(conveyerSystemEdge -> graph.get(conveyerSystemEdge.getSourceGateVertex().getGateName())
                        .getNeighbours()
                        .put(graph.get(conveyerSystemEdge.getDestinationGateVertex().getGateName()),
                                conveyerSystemEdge.getTravelTime()));
        return graph;
    }

    private String constructTravelRoute(List<GateVertex> shortestPath) {
        StringBuilder pathLineBuilder = new StringBuilder();
        shortestPath.stream()
                .filter(Objects::nonNull)
                .forEach(gateVertex -> pathLineBuilder.append(gateVertex.getGateName())
                        .append(DelimiterTypes.SPACE.getDelimiter()));
        pathLineBuilder.append(DelimiterTypes.COLON_DELIMITER.getDelimiter())
                .
                        append(DelimiterTypes.SPACE.getDelimiter())
                .append(shortestPath.get(shortestPath.size() - 1).getTravelTime());
        return pathLineBuilder.toString();
    }
}
