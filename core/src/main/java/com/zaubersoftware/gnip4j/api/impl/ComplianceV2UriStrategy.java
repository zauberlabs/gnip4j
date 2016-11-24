package com.zaubersoftware.gnip4j.api.impl;

import java.net.URI;
import java.util.Locale;

import com.zaubersoftware.gnip4j.api.UriStrategy;

public class ComplianceV2UriStrategy implements UriStrategy {

    public static final String BASE_GNIP_STREAM_URI_V2 = "https://gnip-stream.twitter.com/stream/compliance/accounts/%s/publishers/twitter/%s.json?partition=%s";
    public static final String BASE_GNIP_RULES_URI = "https://gnip-api.twitter.com/rules/powertrack/accounts/%s/publishers/twitter/%s.json";
    public static final String GNIP_RULE_VALIDATION_URI = "https://gnip-api.twitter.com/rules/powertrack/accounts/%s/publishers/twitter/%s/validation.json";

    private final String partition;

    public ComplianceV2UriStrategy(final int partition) {
        if (partition < 1 || partition > 8) {
            throw new IllegalArgumentException("Partition must be between 1 and 8 for compliance version 2");
        } else {
            this.partition = Integer.toString(partition);
        }
    }

    @Override
    public URI createStreamUri(final String account, final String streamName, final Integer backfill) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }
        if(backfill != null) {
            throw new IllegalArgumentException("Backfill is not supported at the compliance");
        }
        
        return URI.create(String.format(Locale.ENGLISH, BASE_GNIP_STREAM_URI_V2, account.trim(), streamName.trim(), partition));
    }

    @Override
    public URI createRulesUri(final String account, final String streamName) {
        return URI.create(createRulesBaseUrl(account, streamName));
    }

    @Override
    public URI createRulesDeleteUri(final String account, final String streamName) {
        return URI.create(createRulesBaseUrl(account, streamName) + "?_method=delete");
    }

    @Override
    public URI createRulesValidationUri(final String account, final String streamName) {
        return URI.create(String.format(Locale.ENGLISH, GNIP_RULE_VALIDATION_URI, account.trim(), streamName.trim()));
    }

    @Override
    public String getHttpMethodForRulesDelete() {
        return UriStrategy.HTTP_POST;
    }

    private String createRulesBaseUrl(final String account, final String streamName) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }

        return String.format(Locale.ENGLISH, BASE_GNIP_RULES_URI, account.trim(), streamName.trim());
    }

    public String getPartition() {
        return partition;
    }
}
