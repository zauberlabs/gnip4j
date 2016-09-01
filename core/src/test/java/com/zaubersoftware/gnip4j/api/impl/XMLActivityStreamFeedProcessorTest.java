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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.StreamNotificationAdapter;
import com.zaubersoftware.gnip4j.api.impl.formats.ActivityUnmarshaller;
import com.zaubersoftware.gnip4j.api.impl.formats.FeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
import com.zaubersoftware.gnip4j.api.impl.formats.XMLActivityStreamFeedProcessor;
import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Test unmarshaling the atom feed
 * 
 * @author Juan F. Codagnone
 * @since Dec 11, 2012
 */
public class XMLActivityStreamFeedProcessorTest {

    /** test */
    @Test
    public final void test() throws IOException, ParseException {
        final InputStream is = getClass().getResourceAsStream("fanpage.xml");
        try {
            final AtomicInteger i = new AtomicInteger();
            final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();
            final FeedProcessor p = new XMLActivityStreamFeedProcessor<Activity>("foo", new DirectExecuteService(),
                    new StreamNotificationAdapter<Activity>() {
                        @Override
                        public void notify(final Activity activity, final GnipStream stream) {
                            i.incrementAndGet();
                            try {
                                final byte[] data0 = mapper.writeValueAsBytes(activity);
                                final Activity e = mapper.reader(Activity.class).readValue(data0);
                                final byte[] data1 = mapper.writeValueAsBytes(e);
                                assertArrayEquals(data0, data1);
                                
                                // test serialization
                                final ObjectOutputStream os = new ObjectOutputStream(new ByteArrayOutputStream());
                                os.writeObject(activity);
                                os.close();
                            } catch (final Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new ActivityUnmarshaller("hola"));
            p.process(is, null);
            assertEquals(23, i.get());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}

class DirectExecuteService implements ExecutorService {

    @Override
    public void execute(final Runnable command) {
        command.run();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return null;
    }

    @Override
    public Future<?> submit(final Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout,
            final TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException,
            ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout,
            final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}