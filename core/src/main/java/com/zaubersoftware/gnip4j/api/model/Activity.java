/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
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
    @JsonProperty(value = "twitter_extended_entities")
    private TwitterEntities twitterExtendedEntities;
    private long retweetCount;
    private int favoritesCount;
    @JsonProperty(value = "twitter_filter_level")
    private String twitterFilterLevel;
    @JsonProperty(value = "twitter_lang")
    private String twitterLang;
    
    private Gnip gnip;
    private String verb;
    private Date postedTime;
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
    private String caption;
    private String comment;
    @JsonProperty(value = "twitter_quoted_status")
    private Activity twitterQuotedStatus;
    
    @JsonProperty(value = "display_text_range")
    int [] displayTextRange;
    @JsonProperty(value = "long_object")
    Object longObject;
    
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

    public TwitterEntities getTwitterExtendedEntities() {
        return twitterExtendedEntities;
    }

    public void setTwitterExtendedEntities(final TwitterEntities twitterExtendedEntities) {
        this.twitterExtendedEntities = twitterExtendedEntities;
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


    public final Date getPostedTime() {
        return postedTime;
    }


    public final void setPostedTime(final Date value) {
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

    public Activity getTwitterQuotedStatus() {
        return twitterQuotedStatus;
    }
    
    public void setTwitterQuotedStatus(final Activity twitterQuotedStatus) {
        this.twitterQuotedStatus = twitterQuotedStatus;
    }
    
    public void setDisplayTextRange(final int[] displayTextRange) {
        this.displayTextRange = displayTextRange;
    }
    
    public int[] getDisplayTextRange() {
        return displayTextRange;
    }
    
    public void setLongObject(final Object longObject) {
        this.longObject = longObject;
    }
    
    public Object getLongObject() {
        return longObject;
    }
    
    /**
     * https://dev.twitter.com/overview/api/places representation for gnip.
     */
    public static class Location implements Serializable {
        public static final String TW_TYPE_COUNTRY="country";
        public static final String TW_TYPE_ADMIN="admin";
        public static final String TW_TYPE_CITY="city";
        public static final String TW_TYPE_NEIGHBORHOOD="neighborhood";
        public static final String TW_TYPE_POI="poi";

        private static final long serialVersionUID = 1L;
        /**
         * A series of longitude and latitude points, defining a box which will
         * contain the Place entity this bounding box is related to. Each point
         * is an array in the form of [longitude, latitude]. Points are grouped
         * into an array per bounding box. Bounding box arrays are wrapped in
         * one additional array to be compatible with the polygon notation.
         */
        private Geo geo;
        
        /**
         * Name of the country containing this place. Example:
         * In Twitter Raw: "country":"France"
         */
        @JsonProperty("country_code")
        private String countryCode;
        
        /**
         * Full human-readable representation of the place’s name.
         * In Twitter Raw: "full_name":"San Francisco, CA"
         */
        private String displayName;
        
        /**
         * "objectType": "place"
         */
        private String objectType;
        
        /**
         * URL representing the location of additional place metadata for this place.
         * 
         * In Twitter Raw: "url":"https://api.twitter.com/1.1/geo/id/7238f93a3e899af6.json"
         */
        private String link;

        /**
         * Short human-readable representation of the place’s name.
         * In Twitter Raw: "name":"Paris"
         */
        private String name;
        @JsonProperty("twitter_country_code")

        /**
         * Shortened country code representing the country containing this place.
         * In Twitter Raw: "country_code":"FR"
         */
        private String twitterCountryCode;
        
        /**
         * The type of location represented by this place.
         * In Twitter Raw: "place_type":"city"
         */
        @JsonProperty("twitter_place_type")
        private String twitterPlaceType;

        public final Geo getGeo() {
            return geo;
        }

        
        public final void setGeo(final Geo value) {
            geo = value;
        }

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

        public final String getLink() {
            return link;
        }

        public final void setLink(final String value) {
            link = value;
        }


        public final String getName() {
            return name;
        }


        public final void setName(final String name) {
            this.name = name;
        }


        public final String getTwitterCountryCode() {
            return twitterCountryCode;
        }


        public final void setTwitterCountryCode(final String twitterCountryCode) {
            this.twitterCountryCode = twitterCountryCode;
        }


        public final String getTwitterPlaceType() {
            return twitterPlaceType;
        }


        public final void setTwitterPlaceType(final String twitterPlaceType) {
            this.twitterPlaceType = twitterPlaceType;
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
    
    public void setLinks(final List<Links> links) {
        this.links = links;
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
    
    public long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(final int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getTwitterFilterLevel() {
        return twitterFilterLevel;
    }

    public void setTwitterFilterLevel(final String twitterFilterLevel) {
        this.twitterFilterLevel = twitterFilterLevel;
    }

    public String getTwitterLang() {
        return twitterLang;
    }

    public void setTwitterLang(final String twitterLang) {
        this.twitterLang = twitterLang;
    }
    
    public final Object getTarget() {
        return target;
    }

    public final void setTarget(final Object target) {
        this.target = target;
    }
    
    public final String getCaption() {
        return this.caption;
    }
    
    public void setCaption(final String caption) {
        this.caption = caption;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
}
