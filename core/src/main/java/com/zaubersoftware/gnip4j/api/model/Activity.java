/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect
public class Activity implements Serializable {
    private static final long serialVersionUID = -6613328643160393766L;
    
    private InReplyTo inReplyTo;
    private Activity.Location location;
    private Actor actor;
    private Geo geo;
    private Generator generator;
    private Object object;
    private Provider provider;
    @JsonProperty(value = "twitter_entities")
    private TwitterEntities twitterEntities;
    private long retweetCount;
    private Gnip gnip;
    private String verb;
    private XMLGregorianCalendar postedTime;
    private Date created;
    private Date updated;
    private String body;
    private String objectType;
    private String id;
    private String link;
    private Source source;
    private List<Category> categories;
    private List<Links> links;
    private String summary;
    private Author author;
    private String content;
    private String subtitle;
    private String title;
    private Object target;
    private Info info;
    
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
        if(gnip == null) {
            gnip = new Gnip();
        }
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
    public static class Location implements Serializable {
        private static final long serialVersionUID = 1L;
        private Geo geo;
        @JsonProperty(value = "country_code")
        private String countryCode;

        @JsonProperty(value = "twitter_country_code")
        private String twitterCountryCode;

        private String displayName;
        private String objectType;
        private String streetAddress;
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

        public String getTwitterCountryCode() {
            return twitterCountryCode;
        }

        public void setTwitterCountryCode(String twitterCountryCode) {
            this.twitterCountryCode = twitterCountryCode;
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

    public final void setSource(final Source value) {
        source = value;
    }
    
    public final Source getSource() {
        return source;
    }
    
    public final Date getCreated() {
        return created;
    }
    
    public final void setCreated(final Date created) {
        this.created = created;
    }
    

    public final Date getUpdated() {
        return updated;
    }
    
    public final void setUpdated(final Date updated) {
        this.updated = updated;
    }
    
    /** get the categories */
    public final List<Category> getCategoriesRules() {
        if (categories == null) {
            categories = new ArrayList<Category>();
        }
        return categories;
    }
    
    public final List<Links> getLinks() {
        if (links == null) {
            links = new ArrayList<Links>();
        }
        return links;
    }
    
    public final String getSummary() {
        return summary;
    }

    public final void setSummary(final String value) {
        summary = value;
    }

    public final Author getAuthor() {
        return author;
    }
    
    public final void setAuthor(final Author author) {
        this.author = author;
    }

    public final String getContent() {
        return content;
    }
    
    public final void setContent(final String content) {
        this.content = content;
    }
    
    public final String getSubtitle() {
        return subtitle;
    }
    
    public final void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public final void setTitle(final String text) {
        title = text;
    }
    
    public final String getTitle() {
        return title;
    }
    
    public final long getRetweetCount() {
        return retweetCount;
    }

    public final void setRetweetCount(final long retweetCount) {
        this.retweetCount = retweetCount;
    }
    
    public final Object getTarget() {
        return target;
    }

    public final void setTarget(final Object target) {
        this.target = target;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
