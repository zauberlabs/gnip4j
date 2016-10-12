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
package com.zaubersoftware.gnip4j.api.impl.formats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.stats.ModifiableStreamStats;

/**
 * <p>
 * Process Enterprise Data Collector streaming feed.
 * </p>
 * <p>
 * The implementation is a bit hacky... It use regular expressions to detect the
 * begging and ending of the diferents activities.
 * </p>
 * <p>
 * Quoted from: <a href=
 * "http://support.gnip.com/customer/portal/articles/477770-consuming-data-via-the-enterprise-data-collector#STREAMING"
 * >Consuming Data via the Enterprise Data Collector</a> <blockquote>
 * <p>
 * Consuming XML Data
 * </p>
 * <p>
 * Because data will continue to arrive over the same response stream you will
 * need to know when to process a particular chunk of data. When using the HTTP
 * stream Gnip sends the data over the stream with each activity as its own
 * "top-level" node. In other words, activities will not be wrapped in an
 * enclosing {@literal <results>} element as they are during a normal poll.
 * </p>
 * <p>
 * Generally the best approach is to read the data and grab the opening element
 * (e.g. the {@literal <entry>} element when the activities are coming across in Atom
 * format), then continue reading until you see the closing element for that tag
 * ({@literal </entry>}). At this point you've got a fully valid Atom XML entry that can be
 * processed by any standard xml parser.
 * </p>
 * <p>
 * Chunking activities this way is the most straightforward means of processing
 * the stream.
 * </p>
 * <p>
 * For example, the following figure shows what a stream may look like when used
 * with a Twitter Actor - Notices data collector. In this example there are two
 * {@literal <status>} documents. Using the approach outlined above, your stream client
 * would see the first {@literal <status>} element, continue reading until it sees the
 * first {@literal </status>} element, then process that chunk as a single XML document. It
 * would then continue reading at the second {@literal <status>} element and process the
 * second document when it sees the second {@literal </status>} element. 
 * </p>
 * </blockquote>
 * @author Juan F. Codagnone
 * @since Dec 11, 2012
 */
public class XMLActivityStreamFeedProcessor<T> extends BaseFeedProcessor<T> {
    private final Pattern startPattern = Pattern.compile("^[<]entry .*$");
    private final Pattern endPattern = Pattern.compile("^[<][/]entry.*$");
    private final Unmarshaller<T> unmarshaller;

    /** constructor */
    public XMLActivityStreamFeedProcessor(final String streamName, final ExecutorService activityService,
            final StreamNotification<T> notification,
            final Unmarshaller<T> unmarshaller) {
        super(streamName, activityService, notification);
        this.unmarshaller = unmarshaller;
    }

    @Override
    public final void process(final InputStream is, final ModifiableStreamStats stats) throws IOException, ParseException {
        // hack
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String s = null;

        StringBuilder sb = new StringBuilder(6 * 1024);
        while ((s = reader.readLine()) != null) {
            if (startPattern.matcher(s).matches()) {
                if(sb.length() != 0) {
                    sb = new StringBuilder(6 * 1024);
                }
                sb.append(s);
            } else if (endPattern.matcher(s).matches()) {
                sb.append(s);
                handle(unmarshaller.unmarshall(sb.toString()));
            } else {
                sb.append(s);
            }
            if (! s.isEmpty() && stats != null){
            	stats.incrementTransferedActivities();
            }
        }
    }
}
