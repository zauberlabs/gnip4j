/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.impl;

import java.net.URI;
import java.util.Locale;

import com.zaubersoftware.gnip4j.api.UriStrategy;

/**
 * Implementation of {@link UriStrategy} that creates {@link URI}s to connect
 * to PowerTrack V2 Gnip endpoints which are different than those used in DefaultUriStrategy
 * 
 * As of 10/1/2015 PowerTrack V2 is in private beta.
 * 
 * https://stream-data-api.twitter.com/stream/powertrack/accounts/&lt;account&gt;/publishers/twitter/&lt;stream&gt;.json
 * https://data-api.twitter.com/rules/powertrack/accounts/&lt;account&gt;/publishers/twitter/&lt;stream&gt;.json"
 *
 * <p>
 * The base URI format for stream is {@link PowerTrackV2UriStrategy#DEFAULT_STREAM_URL_BASE}
 * <p>
 *
 * <p>
 * The base URI format for rules is {@link PowerTrackV2UriStrategy#DEFAULT_RULE_URL_BASE}
 * <p>
 *
 * @author mjmeyer
 * @since 10/01/15
 */
public final class PowerTrackV2UriStrategy implements UriStrategy {
    public static final String DEFAULT_STREAM_URL_BASE  = "https://gnip-stream.twitter.com";
    public static final String DEFAULT_RULE_URL_BASE = "https://gnip-api.twitter.com";
    public static final String PATH_GNIP_STREAM_URI =  "/stream/powertrack/accounts/%s/publishers/%s/%s.json";
    public static final String PATH_GNIP_RULES_URI =  "/rules/powertrack/accounts/%s/publishers/%s/%s.json";
    public static final String PATH_GNIP_RULES_VALIDATION_URI = "/rules/powertrack/accounts/%s/publishers/%s/%s/validation.json";

    private String streamUrlBase = DEFAULT_STREAM_URL_BASE;
    private String ruleUrlBase = DEFAULT_RULE_URL_BASE;

    
    private final String publisher;
    
    /** Creates the DefaultUriStrategy. */
    public PowerTrackV2UriStrategy() {
        this("twitter");
    }
    /** Creates the DefaultUriStrategy. */
    public PowerTrackV2UriStrategy(final String publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("The publisher cannot be null or empty");
        }
        this.publisher = publisher.trim();
    }

    @Override
    public URI createStreamUri(final String account, final String streamName, final Integer backFillMinutes) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }
        if (backFillMinutes != null && (backFillMinutes < 1 || backFillMinutes > 5)) {
            throw new IllegalArgumentException("If set, the backfill parameter must be assigned a value between 1 and 5 (inclusive)");
        }
        
        final StringBuilder sb = new StringBuilder(60);
        sb.append(String.format(Locale.ENGLISH, streamUrlBase + PATH_GNIP_STREAM_URI, account.trim(), publisher, streamName.trim()));
        if (backFillMinutes != null) {
            sb.append(String.format(Locale.ENGLISH, "?backfillMinutes=%s", backFillMinutes));
        }
        return URI.create(sb.toString());
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
    public URI createRulesValidationUri(String account, String streamName) {
        return URI.create(String.format(Locale.ENGLISH, DEFAULT_RULE_URL_BASE + PATH_GNIP_RULES_VALIDATION_URI, account.trim(), publisher, streamName.trim()));
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
         
         return String.format(Locale.ENGLISH, ruleUrlBase + PATH_GNIP_RULES_URI, account.trim(), publisher, streamName.trim());
    }
    
    public final String getStreamUrlBase() {
        return streamUrlBase;
    }
    
    public final void setStreamUrlBase(final String streamUrlBase) {
        if(streamUrlBase == null) {
            throw new IllegalArgumentException("streamUrlBase can't be null");
        }
        this.streamUrlBase = streamUrlBase;
    }
    
    public final String getRuleUrlBase() {
        return ruleUrlBase;
    }
    
    public final void setRuleUrlBase(final  String ruleUrlBase) {
        if(ruleUrlBase == null) {
            throw new IllegalArgumentException("streamUrlBase can't be null");
        }
        this.ruleUrlBase = ruleUrlBase;
    }
    
    
}
