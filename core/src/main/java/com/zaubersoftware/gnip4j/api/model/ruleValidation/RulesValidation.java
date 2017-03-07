package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RulesValidation implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "summary")
    private RuleValidationSummary summary;

    @JsonProperty(value = "detail")
    private List<RuleValidation> detail;

    public RuleValidationSummary getSummary() {
        return summary;
    }

    public void setSummary(final RuleValidationSummary summary) {
        this.summary = summary;
    }

    public List<RuleValidation> getDetail() {
        return detail;
    }

    public void setValidations(final List<RuleValidation> detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return new StringBuilder("RulesValidation[")
            .append("summary=")
            .append(summary)
            .append(' ')
            .append("detail=")
            .append(detail)
            .append(']')
            .toString();
    }
}
