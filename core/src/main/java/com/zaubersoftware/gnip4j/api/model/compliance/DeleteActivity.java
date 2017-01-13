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

import java.io.UncheckedIOException;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Actor;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Represents deleting a status from the Compliance v2 stream
 *
 * @author Dennis Lloyd Jr
 * @since Oct 14, 2016
 */
@JsonTypeName(ComplianceActivity.Verb.DELETE)
class DeleteActivity implements ComplianceActivity {
    @JsonProperty("status")
    private Status status;
    @JsonProperty("favorite")
    private Favorite favorite;
    @JsonProperty("timestamp_ms")
    private String timestamp;

    public static class Status {
        @JsonProperty("id")
        private String id;
        @JsonProperty("user_id")
        private String userId;
    }

    public static class Favorite {
        @JsonProperty("id")
        private String id;
        @JsonProperty("user_id")
        private String userId;
    }

    @Override
    public Activity toActivity() {
        final Activity result = new Activity();

        if(status != null) {
            result.setVerb(ComplianceActivity.Verb.DELETE);
            result.setId(status.id);
            result.setActor(new Actor());
            result.getActor().setId(status.userId);
        } else if (favorite != null) {
            result.setVerb(ComplianceActivity.Verb.FAVORITE_DELETE);
            result.setId(favorite.id);
            result.setActor(new Actor());
            result.getActor().setId(favorite.userId);
        } else {
            throw new UncheckedIOException(new JsonMappingException("Expected delete of 'status' or 'favorite'"));
        }

        result.setUpdated(new Date(Long.parseLong(timestamp)));
        return result;
    }
}
