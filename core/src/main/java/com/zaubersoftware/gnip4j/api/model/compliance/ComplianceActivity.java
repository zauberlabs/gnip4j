/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.model.compliance;

import com.zaubersoftware.gnip4j.api.model.Activity;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;

/**
 * Activity from the Compliance v2 stream that can be converted to an Activity
 *
 * @author Dennis Lloyd Jr
 * @since Oct 14, 2016
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DeleteActivity.class, name = ComplianceActivity.Verb.DELETE),
    @JsonSubTypes.Type(value = DeleteUserActivity.class, name = ComplianceActivity.Verb.USER_DELETE),
    @JsonSubTypes.Type(value = UndeleteUserActivity.class, name = ComplianceActivity.Verb.USER_UNDELETE),
    @JsonSubTypes.Type(value = ScrubGeoActivity.class, name = ComplianceActivity.Verb.SCRUB_GEO),
    @JsonSubTypes.Type(value = UserProtectActivity.class, name = ComplianceActivity.Verb.USER_PROTECT),
    @JsonSubTypes.Type(value = UserUnprotectActivity.class, name = ComplianceActivity.Verb.USER_UNPROTECT),
    @JsonSubTypes.Type(value = UserSuspendActivity.class, name = ComplianceActivity.Verb.USER_SUSPEND),
    @JsonSubTypes.Type(value = UserUnsuspendActivity.class, name = ComplianceActivity.Verb.USER_UNSUSPEND),
    @JsonSubTypes.Type(value = StatusWithheldActivity.class, name = ComplianceActivity.Verb.STATUS_WITHHELD),
    @JsonSubTypes.Type(value = UserWithheldActivity.class, name = ComplianceActivity.Verb.USER_WITHHELD)
})
public interface ComplianceActivity extends Serializable {
    Activity toActivity();

    interface Verb {
        String DELETE = "delete";
        String FAVORITE_DELETE = "favorite_delete";
        String USER_DELETE = "user_delete";
        String USER_UNDELETE = "user_undelete";
        String SCRUB_GEO = "scrub_geo";
        String USER_PROTECT = "user_protect";
        String USER_UNPROTECT = "user_unprotect";
        String USER_SUSPEND = "user_suspend";
        String USER_UNSUSPEND = "user_unsuspend";
        String STATUS_WITHHELD = "status_withheld";
        String USER_WITHHELD = "user_withheld";
    }
}
