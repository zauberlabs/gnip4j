package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return "RuleValidationSummary[" + "valid=" + valid + ' ' + "notValid=" + notValid + "]";
    }
}
