package com.amruth.airport.baggage.routing.models;


/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class Departure {

    private String flightId;
    private GateVertex flightGate;
    private String destination;
    private String flightTime;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public GateVertex getFlightGate() {
        return flightGate;
    }

    public void setFlightGate(GateVertex flightGate) {
        this.flightGate = flightGate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }
}
