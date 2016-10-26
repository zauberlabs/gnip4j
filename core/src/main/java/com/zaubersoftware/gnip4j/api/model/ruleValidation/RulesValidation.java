package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class RulesValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    /* Example Json returned by the validation end-point
    {"summary":{"valid":1,"not_valid":1},
    "detail":[{"valid":true,"rule":{"tag":"AnotherTag,456","value":"(abc OR def) lang:SV"}},
    {"valid":false,"rule":{"tag":"ExampleTag,123","value":"abc AND 123"},"message":"Ambiguous use of and as a keyword. Use a space to logically join two clauses, or \"and\" to find occurrences of and in text (at position 5)\n"}],
    "sent":"2016-10-26T05:09:49.947Z"}
     */

    @JsonProperty(value = "summary")
    private RuleValidationSummary summary;

    @JsonProperty(value = "detail")
    private List<RuleValidation> validations;

    public RuleValidationSummary getSummary() {
        return summary;
    }

    public void setSummary(RuleValidationSummary summary) {
        this.summary = summary;
    }

    public List<RuleValidation> getValidations() {
        return validations;
    }

    public void setValidations(List<RuleValidation> validations) {
        this.validations = validations;
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("RulesValidation[");
        s.append("summary=").append(getSummary()).append(" ");
        s.append("detail=").append(getValidations());
        s.append("]");
        return s.toString();
    }
}
