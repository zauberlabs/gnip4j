package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RuleValidationSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "valid")
    private Integer valid;

    @JsonProperty(value = "not_valid")
    private Integer notValid;

    public Integer getValid() {
        return valid;
    }

    public void setValid(final Integer valid) {
        this.valid = valid;
    }

    public Integer getNotValid() {
        return notValid;
    }

    public void setNotValid(final Integer notValid) {
        this.notValid = notValid;
    }

    @Override
    public String toString() {
        return new StringBuilder("RuleValidationSummary[")
                .append("valid=")
                .append(valid)
                .append(' ')
                .append("notValid=")
                .append(notValid)
                .append("]")
                .toString();
    }
}
