package com.amruth.airport.baggage.routing.models;


/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class ConveyerSystemEdge {

    private GateVertex sourceGateVertex;
    private GateVertex destinationGateVertex;
    private Integer travelTime;

    public GateVertex getSourceGateVertex() {
        return sourceGateVertex;
    }

    public void setSourceGateVertex(GateVertex sourceGateVertex) {
        this.sourceGateVertex = sourceGateVertex;
    }

    public GateVertex getDestinationGateVertex() {
        return destinationGateVertex;
    }

    public void setDestinationGateVertex(GateVertex destinationGateVertex) {
        this.destinationGateVertex = destinationGateVertex;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }
}
