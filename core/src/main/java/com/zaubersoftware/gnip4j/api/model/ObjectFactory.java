
package com.zaubersoftware.gnip4j.api.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.zaubersoftware.gnip4j.api.model package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public final class ObjectFactory {

    private final static QName _Languages_QNAME = new QName("https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", "languages");
    private final static QName _Indices_QNAME = new QName("https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", "indices");
    private final static QName _Value_QNAME = new QName("https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", "value");
    private final static QName _Coordinates_QNAME = new QName("https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", "coordinates");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.zaubersoftware.gnip4j.api.model
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Generator }
     *
     */
    public Generator createGenerator() {
        return new Generator();
    }

    /**
     * Create an instance of {@link Rule }
     *
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link Rules }
     *
     */
    public Rules createRules() {
        return new Rules();
    }

    /**
     * Create an instance of {@link Urls }
     *
     */
    public Urls createUrls() {
        return new Urls();
    }

    /**
     * Create an instance of {@link Activity.Geo }
     *
     */
    public Geo createActivityGeo() {
        return new Geo();
    }

    /**
     * Create an instance of {@link Activity.Location }
     *
     */
    public Activity.Location createActivityLocation() {
        return new Activity.Location();
    }

    /**
     * Create an instance of {@link MatchingRules }
     *
     */
    public MatchingRules createMatchingRules() {
        return new MatchingRules();
    }

    /**
     * Create an instance of {@link InReplyTo }
     *
     */
    public InReplyTo createInReplyTo() {
        return new InReplyTo();
    }

    /**
     * Create an instance of {@link Actor.Location }
     *
     */
    public Actor.Location createActorLocation() {
        return new Actor.Location();
    }

    /**
     * Create an instance of {@link Hashtags }
     *
     */
    public Hashtags createHashtags() {
        return new Hashtags();
    }

    /**
     * Create an instance of {@link TwitterEntities }
     *
     */
    public TwitterEntities createTwitterEntities() {
        return new TwitterEntities();
    }

    /**
     * Create an instance of {@link Links }
     *
     */
    public Links createLinks() {
        return new Links();
    }

    /**
     * Create an instance of {@link Provider }
     *
     */
    public Provider createProvider() {
        return new Provider();
    }

    /**
     * Create an instance of {@link Activity.Location.Geo }
     *
     */
    public Geo createActivityLocationGeo() {
        return new Geo();
    }

    /**
     * Create an instance of {@link UserMentions }
     *
     */
    public UserMentions createUserMentions() {
        return new UserMentions();
    }

    /**
     * Create an instance of {@link Activity }
     *
     */
    public Activity createActivity() {
        return new Activity();
    }

    /**
     * Create an instance of {@link Gnip }
     *
     */
    public Gnip createGnip() {
        return new Gnip();
    }

    /**
     * Create an instance of {@link Object }
     *
     */
    public Object createObject() {
        return new Object();
    }

    /**
     * Create an instance of {@link Language }
     *
     */
    public Language createLanguage() {
        return new Language();
    }

    /**
     * Create an instance of {@link Actor }
     *
     */
    public Actor createActor() {
        return new Actor();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", name = "languages")
    public JAXBElement<String> createLanguages(final String value) {
        return new JAXBElement<String>(_Languages_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", name = "indices")
    public JAXBElement<Integer> createIndices(final Integer value) {
        return new JAXBElement<Integer>(_Indices_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", name = "value")
    public JAXBElement<String> createValue(final String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "https://labs.zaubersoftware.com/ns/2011/05/gnip4j/activity", name = "coordinates")
    public JAXBElement<Double> createCoordinates(final Double value) {
        return new JAXBElement<Double>(_Coordinates_QNAME, Double.class, null, value);
    }

}
