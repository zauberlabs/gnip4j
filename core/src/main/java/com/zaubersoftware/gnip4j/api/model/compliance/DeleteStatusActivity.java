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
package com.zaubersoftware.gnip4j.api.model.compliance;

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Actor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

import java.util.Date;

/**
 * Represents deleting a status from the Compliance v2 stream
 *
 * @author Dennis Lloyd Jr
 * @since Oct 14, 2016
 */
@JsonTypeName(ComplianceActivity.Verb.DELETE)
class DeleteStatusActivity implements ComplianceActivity {
    @JsonProperty("status")
    private Status status;
    @JsonProperty("timestamp_ms")
    private String timestamp;

    public static class Status {
        @JsonProperty("id")
        private String id;
        @JsonProperty("user_id")
        private String userId;
    }

    @Override
    public Activity toActivity() {
        final Activity result = new Activity();
        result.setVerb(ComplianceActivity.Verb.DELETE);
        result.setId(status.id);
        result.setActor(new Actor());
        result.getActor().setId(status.userId);
        result.setUpdated(new Date(Long.parseLong(timestamp)));
        return result;
    }
}
