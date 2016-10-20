package com.zaubersoftware.gnip4j.api.impl;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * @author Fredrik Olsson
 */
public class PowerTrackV2UriStrategyTest {

    @Test
    public void testCreateStrategy_backfill() throws Exception {
        PowerTrackV2UriStrategy strategy = new PowerTrackV2UriStrategy();
        URI streamUri = strategy.createStreamUri("myAccount", "myStream", 2);
        assertEquals("https://gnip-stream.twitter.com/stream/powertrack/accounts/myAccount/publishers/twitter/myStream.json?backfillMinutes=2", streamUri.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateStrategy_backfillOutOfRangeLow() throws Exception {
        PowerTrackV2UriStrategy strategy = new PowerTrackV2UriStrategy();
        strategy.createStreamUri("myAccount", "myStream", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateStrategy_backfillOutOfRangeHigh() throws Exception {
        PowerTrackV2UriStrategy strategy = new PowerTrackV2UriStrategy();
        strategy.createStreamUri("myAccount", "myStream", 6);
    }

    @Test
    public void testCreateStrategy_noArgs() throws Exception {
        PowerTrackV2UriStrategy strategy = new PowerTrackV2UriStrategy();
        URI streamUri = strategy.createStreamUri("myAccount", "myStream");
        assertEquals("https://gnip-stream.twitter.com/stream/powertrack/accounts/myAccount/publishers/twitter/myStream.json", streamUri.toString());
    }

    @Test
    public void testCreateStrategy_published() throws Exception {
        PowerTrackV2UriStrategy strategy = new PowerTrackV2UriStrategy("myPublisher");
        URI streamUri = strategy.createStreamUri("myAccount", "myStream");
        assertEquals("https://gnip-stream.twitter.com/stream/powertrack/accounts/myAccount/publishers/myPublisher/myStream.json", streamUri.toString());
    }

}