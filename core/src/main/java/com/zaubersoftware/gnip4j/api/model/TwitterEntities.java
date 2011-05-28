package com.zaubersoftware.gnip4j.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
    "urls",
    "userMentions",
    "hashtags"
})
@XmlRootElement(name = "twitter_entities")
@JsonAutoDetect
public final class TwitterEntities {

    @XmlElement(required = true)
    private List<Urls> urls;
    @XmlElement(name = "user_mentions", required = true)
    @JsonProperty(value = "user_mentions")
    private List<UserMentions> userMentions;
    @XmlElement(required = true)
    private List<Hashtags> hashtags;

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
            urls = new ArrayList<Urls>();
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
            userMentions = new ArrayList<UserMentions>();
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
            hashtags = new ArrayList<Hashtags>();
        }
        return hashtags;
    }

}
