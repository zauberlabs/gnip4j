package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 *
 */
public class RuleValidationSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "valid")
    private Integer valid;

    @JsonProperty(value = "not_valid")
    private Integer notValid;

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getNotValid() {
        return notValid;
    }

    public void setNotValid(Integer notValid) {
        this.notValid = notValid;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("RuleValidationSummary[");
        s.append("valid=").append(getValid()).append(" ");
        s.append("notValid=").append(getNotValid());
        s.append("]");
        return s.toString();
    }
}
