package com.zaubersoftware.gnip4j.api.model;

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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "location",
    "languages",
    "links"
})
@XmlRootElement(name = "actor")
public final class Actor {

    @XmlElement(required = true)
    private Actor.Location location;
    private List<String> languages;
    @XmlElement(required = true)
    private List<Links> links;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar postedTime;
    @XmlAttribute(required = true)
    private String displayName;
    @XmlAttribute(required = true)
    private String preferredUsername;
    @XmlAttribute(required = true)
    private int utcOffset;
    @XmlAttribute(required = true)
    private String objectType;
    @XmlAttribute(required = true)
    private int statusesCount;
    @XmlAttribute(required = true)
    private BigInteger listedCount;
    @XmlAttribute(required = false)
    private String twitterTimeZone;
    @XmlAttribute(required = true)
    private BigInteger friendsCount;
    @XmlAttribute(required = true)
    private BigInteger followersCount;
    @XmlAttribute(required = true)
    private String summary;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    private String link;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    private String image;
    @XmlAttribute(required = true)
    private String id;

    /**
     * Gets the value of the location property.
     *
     * @return
     *     possible object is
     *     {@link Actor.Location }
     *
     */
    public Actor.Location getLocation() {
        return location;
    }

    public String getTwitterTimeZone() {
        return twitterTimeZone;
    }

    public void setTwitterTimeZone(final String twitterTimeZone) {
        this.twitterTimeZone = twitterTimeZone;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value
     *     allowed object is
     *     {@link Actor.Location }
     *
     */
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
            languages = new ArrayList<String>();
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
            links = new ArrayList<Links>();
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
    public XMLGregorianCalendar getPostedTime() {
        return postedTime;
    }

    /**
     * Sets the value of the postedTime property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setPostedTime(final XMLGregorianCalendar value) {
        postedTime = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDisplayName(final String value) {
        displayName = value;
    }

    /**
     * Gets the value of the preferredUsername property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPreferredUsername() {
        return preferredUsername;
    }

    /**
     * Sets the value of the preferredUsername property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPreferredUsername(final String value) {
        preferredUsername = value;
    }

    /**
     * Gets the value of the utcOffset property.
     *
     */
    public int getUtcOffset() {
        return utcOffset;
    }

    /**
     * Sets the value of the utcOffset property.
     *
     */
    public void setUtcOffset(final int value) {
        utcOffset = value;
    }

    /**
     * Gets the value of the objectType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setObjectType(final String value) {
        objectType = value;
    }

    /**
     * Gets the value of the statusesCount property.
     *
     */
    public int getStatusesCount() {
        return statusesCount;
    }

    /**
     * Sets the value of the statusesCount property.
     *
     */
    public void setStatusesCount(final int value) {
        statusesCount = value;
    }

    /**
     * Gets the value of the listedCount property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getListedCount() {
        return listedCount;
    }

    /**
     * Sets the value of the listedCount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setListedCount(final BigInteger value) {
        listedCount = value;
    }

    /**
     * Gets the value of the friendsCount property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getFriendsCount() {
        return friendsCount;
    }

    /**
     * Sets the value of the friendsCount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setFriendsCount(final BigInteger value) {
        friendsCount = value;
    }

    /**
     * Gets the value of the followersCount property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getFollowersCount() {
        return followersCount;
    }

    /**
     * Sets the value of the followersCount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setFollowersCount(final BigInteger value) {
        followersCount = value;
    }

    /**
     * Gets the value of the summary property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSummary(final String value) {
        summary = value;
    }

    /**
     * Gets the value of the link property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLink(final String value) {
        link = value;
    }

    /**
     * Gets the value of the image property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setImage(final String value) {
        image = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(final String value) {
        id = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="displayName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="objectType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static final class Location {

        @XmlAttribute(required = true)
        private String displayName;
        @XmlAttribute(required = true)
        private String objectType;

        /**
         * Gets the value of the displayName property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Sets the value of the displayName property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setDisplayName(final String value) {
            displayName = value;
        }

        /**
         * Gets the value of the objectType property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getObjectType() {
            return objectType;
        }

        /**
         * Sets the value of the objectType property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setObjectType(final String value) {
            objectType = value;
        }

    }

}
