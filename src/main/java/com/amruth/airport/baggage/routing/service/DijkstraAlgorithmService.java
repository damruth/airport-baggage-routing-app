package com.amruth.airport.baggage.routing.service;


import com.amruth.airport.baggage.routing.models.ConveyerSystemEdge;

import java.util.List;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public interface DijkstraAlgorithmService {
    /**
     * @param source      -   Origin Gate Vertex
     * @param destination - Target Gate Vertex
     * @param graphEdges  -  Conveyer System Edge
     * @return Shortest Path
     */
    String findShortestPath(String source, String destination, List<ConveyerSystemEdge> graphEdges);
}
