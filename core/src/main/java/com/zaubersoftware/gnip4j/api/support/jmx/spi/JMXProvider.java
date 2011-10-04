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
package com.zaubersoftware.gnip4j.api.support.jmx.spi;

import com.zaubersoftware.gnip4j.api.GnipStream;
import com.zaubersoftware.gnip4j.api.stats.StreamStats;

/**
 * JMX Provider
 * 
 * @author Juan F. Codagnone
 * @since May 26, 2011
 */
public interface JMXProvider  {

    /**
     * @param stream
     * @param streamStats
     */
    void registerBean(GnipStream stream, StreamStats streamStats);

    /** @param stream */
    void unregister(GnipStream stream);
}
