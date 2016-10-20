package com.zaubersoftware.gnip4j.api.impl;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;

public class ComplianceV2UriStrategyTest {
    @Test
    public void uriConstructedFromAccountAndStream() {
        final ComplianceV2UriStrategy strategy = new ComplianceV2UriStrategy(1);
        final URI streamUri = strategy.createStreamUri("my-account", "my-stream");
        assertEquals("https://gnip-stream.twitter.com/stream/compliance/accounts/my-account/publishers/twitter/my-stream.json?partition=1", streamUri.toString());

        final URI rulesUri = strategy.createRulesUri("my-account", "my-stream");
        assertEquals("https://gnip-api.twitter.com/rules/powertrack/accounts/my-account/publishers/twitter/my-stream.json", rulesUri.toString());
    }

    @Test
    public void supportsPartitionsBetweenOneAndEight() {
        // Allowed
        for (int partitionNumber = 1; partitionNumber <= 8; partitionNumber++) {
            final ComplianceV2UriStrategy strategy = new ComplianceV2UriStrategy(partitionNumber);
            final String streamUri = strategy.createStreamUri("my-account", "my-stream").toString();
            assertEquals(Integer.toString(partitionNumber), streamUri.substring(streamUri.length() - 1, streamUri.length()));
        }

        // Illegal
        for (final int partitionNumber : new int[]{0, 9, 13}) {
            try {
                new ComplianceV2UriStrategy(partitionNumber);
                fail("Should have thrown IllegalArgumentException for partition: " + partitionNumber);
            } catch (final IllegalArgumentException iae) {
                // We expect this exception
            }
        }
    }
}
