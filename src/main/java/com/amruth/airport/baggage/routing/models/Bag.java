package com.amruth.airport.baggage.routing.models;


/**
 * ==================================================================================================
 * Author: Amruth Deshmukh
 * Date: 03-31-2018
 * ==================================================================================================
 */

public class Bag {

    private String bagNumber;
    private GateVertex entryPointGate;
    private String flightId;

    public String getBagNumber() {
        return bagNumber;
    }

    public void setBagNumber(String bagNumber) {
        this.bagNumber = bagNumber;
    }

    public GateVertex getEntryPointGate() {
        return entryPointGate;
    }

    public void setEntryPointGate(GateVertex entryPointGate) {
        this.entryPointGate = entryPointGate;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
}
