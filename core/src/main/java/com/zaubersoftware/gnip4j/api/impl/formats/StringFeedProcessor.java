/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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

import com.zaubersoftware.gnip4j.api.StreamNotification;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Feed processor that performs no parsing of each activity but instead
 * passes along the activity as a {@link java.lang.String String} as it 
 * was read off the stream.
 */
public class StringFeedProcessor extends BaseFeedProcessor<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	public StringFeedProcessor(String streamName, ExecutorService activityService, StreamNotification<String> notification) {
		super(streamName, activityService, notification);
	}

	@Override
	public void process(InputStream is) throws IOException, ParseException {
		BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		
        logger.debug("Starting to consume activity stream {} ...", streamName);
        while (!Thread.interrupted()) {
        	final String activity = rdr.readLine();
        	handle(activity);
        }
	}
}
