package com.amruth.airport.baggage.routing.domain;

import org.junit.Assert;
import org.junit.Test;

public class BaggageClaimDelegatorImplTest {

    @Test
    public void findBaggageRoutingPaths() {
        String shortestPath = BaggageClaimDelegatorImpl.newInstance().findBaggageRoutingPaths("inputDataFile.txt");
        Assert.assertNotNull(shortestPath);
        Assert.assertEquals("0001 Concourse_A_Ticketing A5 A1 : 11\n"
                + "0002 A5 A1 A2 A3 A4 : 9\n"
                + "0003 A2 A1 : 1\n"
                + "0004 A8 A9 A10 A5 : 6\n"
                + "0005 A7 A8 A9 A10 A5 BaggageClaim : 12", shortestPath);
    }
}