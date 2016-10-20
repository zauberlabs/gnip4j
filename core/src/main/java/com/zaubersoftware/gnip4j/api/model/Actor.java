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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * An actor  
 * 
 * @author Juan F. Codagnone
 * @since Apr 19, 2013
 */
public final class Actor implements Serializable {
    private static final long serialVersionUID = 1;
    private Actor.Location location;
    private List<String> languages;
    private List<Links> links;
    private Date postedTime;
    private String displayName;
    private String preferredUsername;
    private Integer utcOffset;
    private String objectType;
    private int statusesCount;
    private BigInteger listedCount;
    private String twitterTimeZone;
    private BigInteger friendsCount;
    private BigInteger followersCount;
    private Integer favoritesCount;
    private String summary;
    private String link;
    private String image;
    private String id;
    private boolean verified;
    
    // wordpress activity
    // An md5 hash of the person's email.  
    // This value can be used to get the person's gravatar. (http://www.gravatar.com/avatar/<md5>)
    private String wpEmailMd5;
    
    public Actor.Location getLocation() {
        return location;
    }

    public String getTwitterTimeZone() {
        return twitterTimeZone;
    }

    public void setTwitterTimeZone(final String twitterTimeZone) {
        this.twitterTimeZone = twitterTimeZone;
    }

    public void setLocation(final Actor.Location value) {
        location = value;
    }

    /**
     * Gets the value of the languages property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the languages property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguages().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getLanguages() {
        if (languages == null) {
            languages = new ArrayList<>();
        }
        return languages;
    }

    /**
     * Gets the value of the links property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the links property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinks().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Links }
     *
     *
     */
    public List<Links> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }
        return links;
    }

    /**
     * Gets the value of the postedTime property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public Date getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(final Date value) {
        postedTime = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String value) {
        displayName = value;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(final String value) {
        preferredUsername = value;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(final Integer value) {
        utcOffset = value;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(final String value) {
        objectType = value;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(final int value) {
        statusesCount = value;
    }

    public BigInteger getListedCount() {
        return listedCount;
    }

    public void setListedCount(final BigInteger value) {
        listedCount = value;
    }

    public BigInteger getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(final BigInteger value) {
        friendsCount = value;
    }

    public BigInteger getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(final BigInteger value) {
        followersCount = value;
    }
    
    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(final Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String value) {
        summary = value;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String value) {
        link = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String value) {
        image = value;
    }

    
    public String getWpEmailMd5() {
        return wpEmailMd5;
    }

    public void setWpEmailMd5(final String wpEmailMd5) {
        this.wpEmailMd5 = wpEmailMd5;
    }

    public String getId() {
        return id;
    }

    
    public void setId(final String value) {
        id = value;
    }

    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(final boolean verified) {
        this.verified = verified;
    }

    public static final class Location implements Serializable {
        
        private String displayName;
        private String objectType;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(final String value) {
            displayName = value;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(final String value) {
            objectType = value;
        }
    }
    
    
}
