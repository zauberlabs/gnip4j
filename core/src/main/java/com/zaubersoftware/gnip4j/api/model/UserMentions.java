/**
 * Copyright (c) 2011 Zauber S.A. <http://www.zaubersoftware.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indices"
})
@XmlRootElement(name = "user_mentions")
@JsonAutoDetect
public final class UserMentions implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @XmlElement(type = Integer.class)
    private List<Integer> indices;
    @XmlAttribute(name = "screen_name", required = true)
    @JsonProperty(value = "screen_name")
    private String screenName;
    @XmlAttribute(name = "id_str", required = true)
    @JsonProperty(value = "id_str")
    private String idStr;
    @XmlAttribute(required = true)
    private String name;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    private BigInteger id;

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
        return indices;
    }

    /**
     * Gets the value of the screenName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Sets the value of the screenName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setScreenName(final String value) {
        screenName = value;
    }

    /**
     * Gets the value of the idStr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdStr() {
        return idStr;
    }

    /**
     * Sets the value of the idStr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIdStr(final String value) {
        idStr = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(final String value) {
        name = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setId(final BigInteger value) {
        id = value;
    }

}
