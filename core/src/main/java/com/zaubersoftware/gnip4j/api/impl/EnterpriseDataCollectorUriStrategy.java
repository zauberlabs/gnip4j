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
 * to the Enterprise Data Collector
 *
 * @author Juan F. Codagnone
 * @since Dec 11, 2012
 */
public final class EnterpriseDataCollectorUriStrategy implements UriStrategy {
    /** url for an enterprise data collector feed stream */
    public static final String STREAM_URI = 
            "https://%s.gnip.com/data_collectors/%s/stream.xml";
    /** rules url */
    public static final String RULES_URI = "https://%s.gnip.com/data_collectors/%s/rules.json";
    
    @Override
    public URI createStreamUri(final String account, final String streamName, final Integer backfill) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }
        if(backfill != null) {
            throw new IllegalArgumentException("Backfill is not supported at EDC");
        }
        
        return URI.create(String.format(Locale.ENGLISH, STREAM_URI, account.trim(), streamName.trim()));
    }

    @Override
    public URI createRulesUri(final String account, final String streamName) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("The account cannot be null or empty");
        }
        if (streamName == null || streamName.trim().isEmpty()) {
            throw new IllegalArgumentException("The streamName cannot be null or empty");
        }
        
        return URI.create(String.format(Locale.ENGLISH, RULES_URI, account.trim(), streamName.trim()));
    }
    
    @Override
    public URI createRulesDeleteUri(final String account, final String streamName) {
    	return createRulesUri(account, streamName);
    }

    @Override
    public URI createRulesValidationUri(final String account, final String streamName) {
        // There currently is no rule validation end-point for managed sources.
        return null;
    }

    @Override
	public String getHttpMethodForRulesDelete() {
		return UriStrategy.HTTP_DELETE;
	}

}
