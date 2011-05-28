package com.zaubersoftware.gnip4j.api.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indices"
})
@XmlRootElement(name = "urls")
public final class Urls {

    @XmlElement(type = Integer.class)
    private List<Integer> indices;
    @XmlAttribute(name = "expanded_url", required = true)
    @XmlSchemaType(name = "anyURI")
    @JsonProperty(value = "expanded_url")
    private String expandedUrl;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    private String url;
    @XmlAttribute(required = false)
    @XmlSchemaType(name = "display_url")
    @JsonProperty("display_url")
    private String displayUrl;
    
    
    /**
     * Gets the value of the indices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getIndices() {
        if (indices == null) {
            indices = new ArrayList<Integer>();
        }
        return this.indices;
    }

    /**
     * Gets the value of the expandedUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpandedUrl() {
        return expandedUrl;
    }

    /**
     * Sets the value of the expandedUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpandedUrl(String value) {
        this.expandedUrl = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(final String value) {
        this.url = value;
    }

    /**
     * Returns the displayUrl.
     * 
     * @return <code>String</code> with the displayUrl.
     */
    public String getDisplayUrl() {
        return displayUrl;
    }
    /**
     * Sets the displayUrl. 
     *
     * @param displayUrl <code>String</code> with the displayUrl.
     */
    public void setDisplayUrl(final String displayUrl) {
        this.displayUrl = displayUrl;
    }
    
}
