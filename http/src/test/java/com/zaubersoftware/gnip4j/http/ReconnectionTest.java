/**
 * Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.exception.AuthenticationGnipException;
import com.zaubersoftware.gnip4j.api.exception.GnipException;
import com.zaubersoftware.gnip4j.api.exception.TransportGnipException;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Re connection algorithm test
 *
 * @author Guido Marucci Blas
 * @since May 9, 2011
 */
public final class ReconnectionTest {
    /** test */
    @Test(timeout = 10000)
    public void testReConnection() throws Exception {
        // ignore framework warnings
        final Logger root = Logger.getRootLogger();
        root.setLevel(Level.OFF);

        final AtomicInteger count = new AtomicInteger(0);
        final HttpGnipStream stream = new HttpGnipStream(
                new MockRemoteResourceProvider(), "test", 1, new MockExecutorService());
        final StringBuilder out = new StringBuilder();
        final StreamNotification n = new StreamNotification() {
            @Override
            public void notifyReConnectionError(final GnipException e) {
                out.append(String.format("ReConnectionError: %s\n",
                        e.getMessage()));
            }

            @Override
            public void notifyReConnectionAttempt(final int attempt,
                    final long waitTime) {
                out.append(String.format(
                        "Connection attempt %d wait time %d\n", attempt,
                        waitTime));
            }

            @Override
            public void notifyConnectionError(final TransportGnipException e) {
                out.append(String.format("ConnectionError: %s\n",
                        e.getMessage()));
            }

            @Override
            public void notify(final Activity activity, final GnipStream stream) {
                out.append(activity.getBody() + "\n");
                if (count.incrementAndGet() >= 4) {
                    stream.close();
                }
            }
        };
        stream.openAndAwait(n);
        final String s = out.toString();
        final String expected = IOUtils.toString(getClass().getClassLoader()
                .getResourceAsStream("reconnectlog.txt"));
        Assert.assertEquals(expected, s);
    }
}

/** mock implementation */
class MockRemoteResourceProvider implements RemoteResourceProvider {
    private final AtomicInteger i = new AtomicInteger();
    private final List<HttpResponse> responses = new ArrayList<HttpResponse>();

    /** Creates the MockRemoteResourceProvider. */
    public MockRemoteResourceProvider() {
        final ActivityNetworkExceptionInputStream instream = new ActivityNetworkExceptionInputStream(
                "payload-example-2.js", 3525);
        final StatusLine statusLine = new BasicStatusLine(new ProtocolVersion(
                "HTTP", 1, 1), 200, "");
        final HttpEntity entity = new InputStreamEntity(instream, 10000);
        final HttpResponse response = new BasicHttpResponse(statusLine);
        response.setEntity(entity);
        responses.add(response);

        responses.add(new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1),
                505, "TEST!"));
        responses.add(new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1),
                505, "TEST!"));
        responses.add(new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1),
                505, "TEST!"));

        response.setEntity(new InputStreamEntity(
                new ActivityNetworkExceptionInputStream("payload-example-2.js", 3525),
                10000));

        final ActivityNetworkExceptionInputStream instream2 = new ActivityNetworkExceptionInputStream(
                "payload-example-2.js", 3525);
        final StatusLine statusLine2 = new BasicStatusLine(new ProtocolVersion(
                "HTTP", 1, 1), 200, "");
        final HttpEntity entity2 = new InputStreamEntity(instream, 10000);
        final HttpResponse response2 = new BasicHttpResponse(statusLine2);
        response2.setEntity(entity2);
        responses.add(response2);
    }

    @Override
    public HttpResponse getResouce(final URI uri)
            throws AuthenticationGnipException, TransportGnipException {
        final HttpResponse response = responses.get(i.getAndIncrement());
        
        final StatusLine statusLine = response.getStatusLine();
        final int statusCode = statusLine.getStatusCode();
        if (statusCode == 401) {
            throw new AuthenticationGnipException(statusLine.getReasonPhrase());
        } else if (statusCode == 200) {
            return response;
        } else {
            throw new TransportGnipException(
                String.format("Connection to %s: Unexpected status code: %s %s",
                        uri, statusCode, statusLine.getReasonPhrase()));
        }
        
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
