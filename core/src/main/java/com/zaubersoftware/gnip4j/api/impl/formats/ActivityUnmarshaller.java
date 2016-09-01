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

import java.io.StringReader;
import java.text.ParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 *  Unmarshalls Gnip's Activity from an Activity XML Stream
 * 
 * @author Juan F. Codagnone
 * @since Oct 3, 2013
 */
public class ActivityUnmarshaller implements Unmarshaller<Activity> {
    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String streamName;
    
    
    /** @param streamName just for the info */
    public ActivityUnmarshaller(final String streamName) {
        this.streamName = streamName;
    }
    
    /** parse */
    @Override
    public final Activity unmarshall(final String s)  {
        try {
            return unmarshallInternal(s);
        } catch (final XMLStreamException e) {
            throw new IllegalArgumentException("parsing activity", e);
        } catch (final ParseException e) {
            throw new IllegalArgumentException("parsing activity", e);
        }
    }
    
    /** parse  */
    public final Activity unmarshallInternal(final String s) throws XMLStreamException, ParseException {
        final XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(s));
        final AtomFeedParser parser = new AtomFeedParser();
        try {
            final Activity activity = parser.process(reader);
            
            if (activity == null) {
                logger.warn("Activity parsed from stream {} is null. Should not happen!",
                        streamName);
                return null;
            }
            if (activity.getBody() == null && activity.getObject() == null) {
                logger.warn("{}: Activity with id {} and link {} has a null body",
                        new Object[]{streamName, activity.getId(), activity.getLink()});
            }
            logger.trace("{}: Notifying activity {}", streamName, activity.getBody());
            
            return activity;
        } finally {
            reader.close();
        }
    }    
}