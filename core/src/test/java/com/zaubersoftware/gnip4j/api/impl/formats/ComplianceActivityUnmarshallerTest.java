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
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dennis Lloyd Jr
 * @since Oct 14, 2016
 */
public class ComplianceActivityUnmarshallerTest {
    Unmarshaller<Activity> unmarshaller;

    @Before
    public void setup() {
        unmarshaller = new ComplianceActivityUnmarshaller();
    }

    @Test
    public void deleteStatus() {
        final String s = "{\"delete\":{\"status\":{\"id\":601430178305220600,\"id_str\":\"601430178305220600\",\"user_id\":3198576760,\"user_id_str\":\"3198576760\"},\"timestamp_ms\":\"1432228155593\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.DELETE, result.getVerb());
        assertEquals("601430178305220600", result.getId());
        assertEquals("3198576760", result.getActor().getId());
        assertEquals(new Date(1432228155593L), result.getUpdated());
    }

    @Test
    public void deleteUser() {
        final String s = "{\"user_delete\":{\"id\":771136850,\"timestamp_ms\":\"1432228153548\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.USER_DELETE, result.getVerb());
        assertEquals("771136850", result.getActor().getId());
        assertEquals(new Date(1432228153548L), result.getUpdated());
    }

    @Test
    public void undeleteUser() {
        final String s = "{\"user_undelete\":{\"id\":796250066,\"timestamp_ms\":\"1432228149062\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.USER_UNDELETE, result.getVerb());
        assertEquals("796250066", result.getActor().getId());
        assertEquals(new Date(1432228149062L), result.getUpdated());
    }
    
    @Test
    public void scrubGeo() {
        final String s = "{\"scrub_geo\":{\"user_id\":519761961,\"up_to_status_id\":411552403083628540,\"up_to_status_id_str\":\"411552403083628544\",\"user_id_str\":\"519761961\",\"timestamp_ms\":\"1432228180345\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.SCRUB_GEO, result.getVerb());
        assertEquals("411552403083628540", result.getId());
        assertEquals("519761961", result.getActor().getId());
        assertEquals(new Date(1432228180345L), result.getUpdated());
    }

    @Test
    public void userProtect() {
        final String s = "{\"user_protect\":{\"id\":3182003550,\"timestamp_ms\":\"1432228177137\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.USER_PROTECT, result.getVerb());
        assertEquals("3182003550", result.getActor().getId());
        assertEquals(new Date(1432228177137L), result.getUpdated());
    }

    @Test
    public void userSuspend() {
        final String s = "{\"user_suspend\":{\"id\":3120539094,\"timestamp_ms\":\"1432228194217\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.USER_SUSPEND, result.getVerb());
        assertEquals("3120539094", result.getActor().getId());
        assertEquals(new Date(1432228194217L), result.getUpdated());
    }

    @Test
    public void userUnsuspend() {
        final String s = "{\"user_unsuspend\":{\"id\":3293130873,\"timestamp_ms\":\"1432228193828\"}}";
        final Activity result = unmarshaller.unmarshall(s);
        assertEquals(ComplianceActivity.Verb.USER_UNSUSPEND, result.getVerb());
        assertEquals("3293130873", result.getActor().getId());
        assertEquals(new Date(1432228193828L), result.getUpdated());
    }

    @Test
    public void unrecognizedMesssageType() {
        final String s = "{\"unknown\":{\"id\":3293130873,\"timestamp_ms\":\"1432228193828\"}}";
        assertNull(unmarshaller.unmarshall(s));
    }
}
