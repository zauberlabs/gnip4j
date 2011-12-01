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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "inReplyTo", "location", "actor", "geo",
        "generator", "object", "provider", "twitterEntities", "gnip" })
@XmlRootElement(name = "activity")
@JsonAutoDetect
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private InReplyTo inReplyTo;
    @XmlElement(required = true)
    private Activity.Location location;
    @XmlElement(required = true)
    private Actor actor;
    @XmlElement(required = true)
    private Geo geo;
    @XmlElement(required = true)
    private Generator generator;
    @XmlElement(required = true)
    private Object object;
    @XmlElement(required = true)
    private Provider provider;
    @XmlElement(name = "twitter_entities", required = true)
    @JsonProperty(value = "twitter_entities")
    private TwitterEntities twitterEntities;
    @XmlElement(required = true)
    private Gnip gnip;
    @XmlAttribute(required = true)
    private String verb;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar postedTime;
    @XmlAttribute(required = true)
    private String body;
    @XmlAttribute(required = true)
    private String objectType;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    private String id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    private String link;

    public final InReplyTo getInReplyTo() {
        return inReplyTo;
    }

    public final void setInReplyTo(final InReplyTo value) {
        inReplyTo = value;
    }

    public final Activity.Location getLocation() {
        return location;
    }

    public final void setLocation(final Activity.Location value) {
        location = value;
    }

    public final Actor getActor() {
        return actor;
    }

    public final void setActor(final Actor value) {
        actor = value;
    }

    public final Geo getGeo() {
        return geo;
    }

    public final void setGeo(final Geo value) {
        geo = value;
    }

    public final Generator getGenerator() {
        return generator;
    }

    public final void setGenerator(final Generator value) {
        generator = value;
    }

    public final Object getObject() {
        return object;
    }

    public final void setObject(final Object value) {
        object = value;
    }

    public final Provider getProvider() {
        return provider;
    }

    public final void setProvider(final Provider value) {
        provider = value;
    }
    
    public final TwitterEntities getTwitterEntities() {
        return twitterEntities;
    }

    public final void setTwitterEntities(final TwitterEntities value) {
        twitterEntities = value;
    }

    public final Gnip getGnip() {
        return gnip;
    }

    public final void setGnip(final Gnip value) {
        gnip = value;
    }

    public final String getVerb() {
        return verb;
    }

    public final void setVerb(final String value) {
        verb = value;
    }


    public final XMLGregorianCalendar getPostedTime() {
        return postedTime;
    }


    public final void setPostedTime(final XMLGregorianCalendar value) {
        postedTime = value;
    }

    public final String getBody() {
        return body;
    }


    public final void setBody(final String value) {
        body = value;
    }

    public final String getObjectType() {
        return objectType;
    }

    public final void setObjectType(final String value) {
        objectType = value;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String value) {
        id = value;
    }

    public final String getLink() {
        return link;
    }

    public final void setLink(final String value) {
        link = value;
    }

    
    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "geo" })
    public static class Location implements Serializable {
        private static final long serialVersionUID = 1L;
        
        @XmlElement(required = true)
        private Geo geo;
        @XmlAttribute(name = "country_code", required = true)
        private String countryCode;
        @XmlAttribute(required = true)
        private String displayName;
        @XmlAttribute(required = true)
        private String objectType;
        @XmlAttribute(required = true)
        private String streetAddress;
        @XmlAttribute(required = true)
        @XmlSchemaType(name = "anyURI")
        private String link;

        
        public final Geo getGeo() {
            return geo;
        }

        
        public final void setGeo(final Geo value) {
            geo = value;
        }

        /**
         * Gets the value of the countryCode property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public final String getCountryCode() {
            return countryCode;
        }

        public final void setCountryCode(final String value) {
            countryCode = value;
        }

        public final String getDisplayName() {
            return displayName;
        }

        public final void setDisplayName(final String value) {
            displayName = value;
        }

        public final String getObjectType() {
            return objectType;
        }

        public final void setObjectType(final String value) {
            objectType = value;
        }

        public final String getStreetAddress() {
            return streetAddress;
        }

        public final void setStreetAddress(final String value) {
            streetAddress = value;
        }

        public final String getLink() {
            return link;
        }

        public final void setLink(final String value) {
            link = value;
        }
    }

}
