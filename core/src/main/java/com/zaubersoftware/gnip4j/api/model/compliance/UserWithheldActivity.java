package com.zaubersoftware.gnip4j.api.model.compliance;

import com.zaubersoftware.gnip4j.api.model.Activity;
import com.zaubersoftware.gnip4j.api.model.Actor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

import java.util.Collection;
import java.util.Date;

/**
 * Represents a user is withheld in the Compliance v2 Stream
 *
 * @author Dennis Lloyd Jr
 * @since Dec 13, 2016
 */
@JsonTypeName(ComplianceActivity.Verb.USER_WITHHELD)
public class UserWithheldActivity implements ComplianceActivity {
    @JsonProperty("id")
    private String id;

    @JsonProperty("withheld_in_countries")
    private Collection<String> withheldInCountries;

    @JsonProperty("timestamp_ms")
    private String timestamp;

    @Override
    public Activity toActivity() {
        final Activity result = new Activity();
        result.setVerb(ComplianceActivity.Verb.USER_WITHHELD);
        result.setActor(new Actor());
        result.getActor().setId(id);
        result.setWithheldInCountries(withheldInCountries);
        result.setUpdated(new Date(Long.parseLong(timestamp)));
        return result;
    }
}
