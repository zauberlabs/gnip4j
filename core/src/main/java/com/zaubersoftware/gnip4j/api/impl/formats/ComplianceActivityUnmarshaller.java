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

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.compliance.ComplianceActivity;
import com.zaubersoftware.gnip4j.api.support.logging.LoggerFactory;
import com.zaubersoftware.gnip4j.api.support.logging.spi.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Translates JSON input from the Compliance v2 stream into instances of Activity.
 *
 * @see <a href="http://support.gnip.com/sources/twitter/data_format.html#SamplePayloads">Sample Payloads</a>
 *
 * @author Dennis Lloyd Jr
 * @since Oct 14, 2016
 */
public class ComplianceActivityUnmarshaller implements Unmarshaller<Activity> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();

    @Override
    public final Activity unmarshall(final String s) {
        try {
            return mapper.readValue(s, ComplianceActivity.class).toActivity();
        } catch (final IOException | UncheckedIOException e) {
            logger.warn("Failed to parse compliance activity: " + s, e);
            return null;
        }
    }
}
