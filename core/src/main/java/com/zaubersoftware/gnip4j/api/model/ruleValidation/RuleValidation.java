package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zaubersoftware.gnip4j.api.model.Rule;

public class RuleValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "valid")
    private boolean valid;

    @JsonProperty(value = "rule")
    private Rule rule;

    @JsonProperty(value = "message")
    private String message;

    public boolean isValid() {
        return valid;
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(final Rule rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringBuilder("RuleValidation[")
             .append("valid=")
             .append(valid)
             .append(' ')
             .append("rule=")
             .append(rule)
             .append(' ')
             .append("message=")
             .append(getMessage())
             .append(']')
             .toString();
    }
}
