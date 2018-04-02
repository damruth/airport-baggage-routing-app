package com.amruth.airport.baggage.routing.models;

import java.util.List;

/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public final class AirportContext {

    private List<GateVertex> gateVertices;
    private List<ConveyerSystemEdge> conveyerSystemEdges;
    private List<Departure> departures;
    private List<Bag> bags;

    private AirportContext() {

    }

    public static AirportContext newInstance() {
        return new AirportContext();
    }

    public List<GateVertex> getGateVertices() {
        return gateVertices;
    }

    public AirportContext setGateVertices(List<GateVertex> gateVertices) {
        this.gateVertices = gateVertices;
        return this;
    }

    public List<Bag> getBags() {
        return bags;
    }

    public AirportContext setBags(List<Bag> bags) {
        this.bags = bags;
        return this;
    }

    public List<ConveyerSystemEdge> getConveyerSystemEdges() {
        return conveyerSystemEdges;
    }

    public AirportContext setConveyerSystemEdges(List<ConveyerSystemEdge> conveyerSystemEdges) {
        this.conveyerSystemEdges = conveyerSystemEdges;
        return this;
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public AirportContext setDepartures(List<Departure> departures) {
        this.departures = departures;
        return this;
    }
}
