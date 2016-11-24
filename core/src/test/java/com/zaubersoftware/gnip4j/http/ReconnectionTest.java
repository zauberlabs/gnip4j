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

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.RemoteResourceProvider;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.UriStrategy;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.impl.DefaultGnipStream;
import com.zaubersoftware.gnip4j.api.impl.PowerTrackV2UriStrategy;
import com.zaubersoftware.gnip4j.api.impl.formats.ActivityUnmarshaller;
import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Re connection algorithm test
 *
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public final class ReconnectionTest {

    private final UriStrategy uriStrategy = new PowerTrackV2UriStrategy();

    /** test */
    @Test//(timeout = 10000)
    public void testReConnection() throws Exception {
        // ignore framework warnings
        final Logger root = Logger.getRootLogger();
        root.setLevel(Level.OFF);

        final AtomicInteger count = new AtomicInteger(0);
        final DefaultGnipStream stream = new DefaultGnipStream(new MockRemoteResourceProvider(), "account", "stream", new MockExecutorService(), uriStrategy);
        final StringBuilder out = new StringBuilder();
        final StreamNotification<Activity> n = new StreamNotification<Activity>() {
            @Override
            public void notifyReConnectionError(final GnipException e) {
                out.append(String.format(Locale.ENGLISH, "ReConnectionError: %s\n",
                        e.getMessage()));
            }

            @Override
            public void notifyReConnectionAttempt(final int attempt,
                    final long waitTime) {
                out.append(String.format(Locale.ENGLISH, 
                        "Connection attempt %d wait time %d\n", attempt,
                        waitTime));
            }

            @Override
            public void notifyConnectionError(final TransportGnipException e) {
                out.append(String.format(Locale.ENGLISH, "ConnectionError: %s\n",
                        e.getMessage()));
            }

            @Override
            public void notify(final Activity activity, final GnipStream stream) {
                out.append(activity.getBody() + "\n");
                if (count.incrementAndGet() >= 4) {
                    stream.close();
                }
            }

			@Override
			public void notifyReConnected(final int attempt, final long elaspedDisconnectedTime) {
				out.append(String.format(Locale.ENGLISH, 
                        "Connection attempt %d succeeded\n", attempt));
				
			}
        };
        final JsonActivityFeedProcessor processor = new JsonActivityFeedProcessor("stream", Executors.newSingleThreadExecutor(), n);
        processor.setStream(stream);
        stream.open(n, new ActivityUnmarshaller("stream"), processor);
        stream.await();
        final String s = out.toString();
        final String expected = IOUtils.toString(getClass().getClassLoader()
                .getResourceAsStream("reconnectlog.txt"));
        Assert.assertEquals(expected, s);
        Assert.assertEquals("transferedBytes = 8000\ntransferedActivities = 4\n"
                + "numberOfSucessfulReconnections = 1\nnumberOfReconnections = 4",
                stream.getStreamStats().toString());
    }
}

/** mock implementation */
class MockRemoteResourceProvider implements RemoteResourceProvider {
    private final AtomicInteger i = new AtomicInteger();
    private final List<Object []> responses = new ArrayList<>();

    /** Creates the MockRemoteResourceProvider. */
    public MockRemoteResourceProvider() {
        final ActivityNetworkExceptionInputStream instream = new ActivityNetworkExceptionInputStream(
                "payload-example-2.js", 3525);
        final ActivityNetworkExceptionInputStream instream2 = new ActivityNetworkExceptionInputStream(
                "payload-example-2.js", 3525);

        responses.add(new Object[]{200, "Ok", instream});
        responses.add(new Object[]{505, "TEST!", null});
        responses.add(new Object[]{505, "TEST!", null});
        responses.add(new Object[]{505, "TEST!", null});
        responses.add(new Object[]{200, "Ok", instream2});
    }

    @Override
    public InputStream getResource(final URI uri)
            throws AuthenticationGnipException, TransportGnipException {
        final Object []response = responses.get(i.getAndIncrement());

        final int statusCode = (Integer) response[0];
        if (statusCode == 401) {
            throw new AuthenticationGnipException(response[1].toString());
        } else if (statusCode == 200) {
            return (InputStream) response[2];
        } else {
            throw new TransportGnipException(
                String.format(Locale.ENGLISH, "Connection to %s: Unexpected status code: %s %s",
                        uri, statusCode, response[1].toString()));
        }

    }

    @Override
    public <T> T postResource(final URI uri, final Object resource, final Class<T> clazz) {
        return null;
    }

	@Override
	public void deleteResource(final URI uri, final Object resource)
			throws AuthenticationGnipException, TransportGnipException {
		// TODO Auto-generated method stub
		
	}
}

class MockExecutorService implements ExecutorService {

    @Override
    public void execute(final Runnable command) {
        command.run();
    }

    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        execute(task);
        return null;
    }

    @Override
    public Future<?> submit(final Runnable task) {
        execute(task);
        return null;
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        try {
            task.call();
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isTerminated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isShutdown() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks,
            final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(
            final Collection<? extends Callable<T>> tasks, final long timeout,
            final TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(
            final Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }
}
