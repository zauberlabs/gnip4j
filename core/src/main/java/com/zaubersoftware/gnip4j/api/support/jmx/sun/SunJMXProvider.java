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
package com.zaubersoftware.gnip4j.api.support.jmx.sun;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;
import com.zaubersoftware.gnip4j.api.support.jmx.spi.JMXProvider;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;

/**
 * Implementation for standar JDK. 
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public class SunJMXProvider implements JMXProvider {
   private final Logger logger = LoggerFactory.getLogger(getClass());
   
    @Override
    public final void registerBean(final GnipStream stream, final StreamStats streamStats) {
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        // Construct the ObjectName for the Hello MBean we will register
        try {
            final ObjectName mbeanName = getStreamName(stream);
            mbs.registerMBean(new com.zaubersoftware.gnip4j.api.support.jmx.sun.StreamStats(streamStats),
                    mbeanName);
        } catch (final InstanceAlreadyExistsException e) {
            throw new IllegalArgumentException(e);
        } catch (final MBeanRegistrationException e) {
            throw new IllegalArgumentException(e);
        } catch (final NotCompliantMBeanException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** @return the MBean name */
    private ObjectName getStreamName(final GnipStream stream) {
        final String name =  "com.zaubersoftware.gnip4j.streams."
            + stream.getStreamName() + ":type=StreamStats";
        try {
            return new ObjectName(name);
        } catch (final MalformedObjectNameException e) {
            throw new IllegalArgumentException("bad name", e);
        }
    }

    @Override
    public final void unregister(final GnipStream stream) {
        final ObjectName name = getStreamName(stream);
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(name);
        } catch (final MBeanRegistrationException e) {
            throw new RuntimeException(e);
        } catch (final InstanceNotFoundException e) {
            logger.warn("Unknown stream: " + name);
        }
    }
}
