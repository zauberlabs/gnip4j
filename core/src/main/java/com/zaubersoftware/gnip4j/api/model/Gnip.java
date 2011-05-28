package com.zaubersoftware.gnip4j.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "language",
    "matchingRules",
    "urls"
})
@XmlRootElement(name = "gnip")
public class Gnip {

    @XmlElement(required = true)
    private Language language;
    @XmlElement(name = "matching_rules", required = true)
    @JsonProperty(value = "matching_rules")
    private List<MatchingRules> matchingRules;
    @XmlElement(required = true)
    private List<Url> urls;

    /**
     * Gets the value of the language property.
     *
     * @return
     *     possible object is
     *     {@link Language }
     *
     */
    public final Language getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     *
     * @param value
     *     allowed object is
     *     {@link Language }
     *
     */
    public final void setLanguage(final Language value) {
        language = value;
    }

    /**
     * Gets the value of the matchingRules property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the matchingRules property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMatchingRules().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MatchingRules }
     *
     *
     */
    public final List<MatchingRules> getMatchingRules() {
        if (matchingRules == null) {
            matchingRules = new ArrayList<MatchingRules>();
        }
        return matchingRules;
    }

    public final List<Url> getUrls() {
        if (urls  == null) {
            urls  = new ArrayList<Url>();
        }
        return urls;
    }
}
