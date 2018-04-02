package com.amruth.airport.baggage.routing.models;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class GateVertex implements Comparable<GateVertex> {

    private final String gateName;
    //Neighbour Vertices and its weight (Travel Time) map.
    private final Map<GateVertex, Integer> neighbours = new HashMap<>();
    //Total Travel Time to this GateVertex from origin Vertex specified for shortest path.
    private Integer travelTime = Integer.MAX_VALUE;
    private GateVertex previousVertex;

    public GateVertex(String gateName) {
        this.gateName = gateName;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public GateVertex getPreviousVertex() {
        return previousVertex;
    }

    public void setPreviousVertex(GateVertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public Map<GateVertex, Integer> getNeighbours() {
        return neighbours;
    }

    public String getGateName() {
        return gateName;
    }

    public int compareTo(GateVertex other) {
        if (travelTime == other.travelTime)
            return gateName.compareTo(other.gateName);
        return Integer.compare(travelTime, other.travelTime);
    }
}
