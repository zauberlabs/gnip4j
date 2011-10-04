package com.zaubersoftware.gnip4j.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rule"
})
@XmlRootElement(name = "rules")
@JsonAutoDetect
public final class Rules {

    private List<Rule> rules;

    /**
     * Gets the value of the rule property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rule property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRules().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Rule }
     *
     *
     */
    public List<Rule> getRules() {
        if (rules == null) {
            rules = new ArrayList<Rule>();
        }
        return rules;
    }

}
