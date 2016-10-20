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
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
@JsonAutoDetect
public final class TwitterEntities implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Urls> urls;
    @JsonProperty(value = "user_mentions")
    private List<UserMentions> userMentions;
    private List<Hashtags> hashtags;
    @JsonProperty(value = "media") 
    private List<MediaUrls> mediaUrls;

    /**
     * Gets the value of the urls property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the urls property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUrls().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Urls }
     *
     *
     */
    public List<Urls> getUrls() {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        return urls;
    }

    /**
     * Gets the value of the userMentions property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userMentions property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserMentions().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserMentions }
     *
     *
     */
    public List<UserMentions> getUserMentions() {
        if (userMentions == null) {
            userMentions = new ArrayList<>();
        }
        return userMentions;
    }

    /**
     * Gets the value of the hashtags property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hashtags property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHashtags().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Hashtags }
     *
     *
     */
    public List<Hashtags> getHashtags() {
        if (hashtags == null) {
            hashtags = new ArrayList<>();
        }
        return hashtags;
    }
    
    /**
     * Gets the value of the media property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the media property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUrls().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MediaUrls }
     *
     *
     */
    public List<MediaUrls> getMediaUrls() {
        if (mediaUrls == null) {
            mediaUrls = new ArrayList<>();
        }
        return mediaUrls;
    }

}
