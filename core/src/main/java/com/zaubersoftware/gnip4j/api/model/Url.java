package com.zaubersoftware.gnip4j.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * TODO Descripcion de la clase. Los comenterios van en castellano.
 *
 *
 * @author Juan F. Codagnone
 * @since May 28, 2011
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "url",
    "expandedUrl"
})
@XmlRootElement(name = "gnip")
public class Url {
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    private String url;

    @XmlAttribute(required = true, name = "expanded_url")
    @XmlSchemaType(name = "anyURI")
    @JsonProperty(value = "expanded_url")
    private String expandedUrl;

    public final String getUrl() {
        return url;
    }

    public final void setUrl(final String url) {
        this.url = url;
    }

    public final String getExpandedUrl() {
        return expandedUrl;
    }

    public final void setExpandedUrl(final String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }
}
