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
package com.zaubersoftware.gnip4j.http;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipFacade;
import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipFacade;
import com.zaubersoftware.gnip4j.api.impl.ImmutableGnipAuthentication;
import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.support.http.JRERemoteResourceProvider;

/**
 * TODO: Description of the class, Comments in english by default
 *
 *
 * @author Guido Marucci Blas
 * @since 11/11/2011
 */
public final class LocalhostTestDriver {

    @Test
    public void test() throws Exception {
        try {
            final UriStrategy uriStrategy = new UriStrategy() {

                @Override
                public URI createStreamUri(final String domain, final String streamName, final Integer backfill) {
                    return URI.create("http://localhost:8080");
                }

                @Override
                public URI createRulesUri(final String domain, final String streamName) {
                    return null;
                }
                
                @Override
                public URI createRulesDeleteUri(final String domain, final String streamName) {
                    return null;
                }

                @Override
                public URI createRulesValidationUri(final String account, final String streamName) {
                    return null;
                }
                
                @Override
            	public String getHttpMethodForRulesDelete() {
            		return UriStrategy.HTTP_DELETE;
            	}
            };
            final JRERemoteResourceProvider resourceProvider = new JRERemoteResourceProvider(
                    new ImmutableGnipAuthentication("foo", "bar"));
            final GnipFacade gnip = new DefaultGnipFacade(resourceProvider, uriStrategy);

            System.out.println("-- Creating stream");
            final AtomicInteger counter = new AtomicInteger();
            final StreamNotificationAdapter<Activity> n = new StreamNotificationAdapter<Activity>() {
                @Override
                public void notify(final Activity activity, final GnipStream stream) {
                    final int i = counter.getAndIncrement();
                    if (i >= 100000) {
                        System.out.println("-- Closing stream.");
                        stream.close();
                    }
                    System.out.println(i + "-" + activity.getBody() + " " + activity.getGnip().getMatchingRules());
                }
            };
            final GnipStream stream = gnip.createPowertrackStream(Activity.class).withAccount("test-account")
                                                                    .withType("test-stream")
                                                                    .withObserver(n)
                                                                    .build();
            System.out.println("-- Awaiting for stream to terminate");
            stream.await();
            System.out.println("-- Shutting down");

        }   catch(final Throwable t) {
            System.out.println(t.getMessage());
            t.printStackTrace();
        }
    }
}
