package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import com.zaubersoftware.gnip4j.api.model.Rule;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 *
 */
public class RuleValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "valid")
    private Boolean valid;

    @JsonProperty(value = "rule")
    private Rule rule;

    @JsonProperty(value = "message")
    private String message;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("RuleValidation[");
        s.append("valid=").append(getValid()).append(" ");
        s.append("rule=").append(getRule()).append(" ");
        s.append("message=").append(getMessage());
        s.append("]");
        return s.toString();
    }
}
