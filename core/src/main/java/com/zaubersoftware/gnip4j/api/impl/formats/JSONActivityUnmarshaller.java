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

import java.io.IOException;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.zaubersoftware.gnip4j.api.model.Activity;

/**
 * Unmarshaller  
 * 
 * 
 * @author Juan F. Codagnone
 * @since Mar 10, 2014
 */
public class JSONActivityUnmarshaller implements Unmarshaller<Activity> {
    private final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();
    
    @Override
    public final Activity unmarshall(final String s) {
        try {
            return mapper.readValue(s, Activity.class);
        } catch (final JsonMappingException e) {
            throw new IllegalArgumentException("parsing activity", e);
        } catch (final IOException e) {
            throw new IllegalArgumentException("parsing activity", e);
        }
    }
}
