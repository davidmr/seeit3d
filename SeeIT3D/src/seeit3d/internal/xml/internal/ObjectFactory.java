//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.14 at 06:31:01 PM COT 
//


package seeit3d.internal.xml.internal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the seeit3d.modelers.xml.internal package. 
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
public class ObjectFactory {

    private final static QName _Name_QNAME = new QName("http://dl.dropbox.com/u/2416325/seeit3d_xsd", "name");
    private final static QName _MetricName_QNAME = new QName("http://dl.dropbox.com/u/2416325/seeit3d_xsd", "metricName");
    private final static QName _Value_QNAME = new QName("http://dl.dropbox.com/u/2416325/seeit3d_xsd", "value");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: seeit3d.modelers.xml.internal
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Container }
     * 
     */
    public Container createContainer() {
        return new Container();
    }

    /**
     * Create an instance of {@link Containers }
     * 
     */
    public Containers createContainers() {
        return new Containers();
    }

    /**
     * Create an instance of {@link MetricsValue }
     * 
     */
    public MetricsValue createMetricsValue() {
        return new MetricsValue();
    }

    /**
     * Create an instance of {@link EntryMetricValue }
     * 
     */
    public EntryMetricValue createEntryMetricValue() {
        return new EntryMetricValue();
    }

    /**
     * Create an instance of {@link MetricsList }
     * 
     */
    public MetricsList createMetricsList() {
        return new MetricsList();
    }

    /**
     * Create an instance of {@link Mapping }
     * 
     */
    public Mapping createMapping() {
        return new Mapping();
    }

    /**
     * Create an instance of {@link MetricDescription }
     * 
     */
    public MetricDescription createMetricDescription() {
        return new MetricDescription();
    }

    /**
     * Create an instance of {@link Polycylinder }
     * 
     */
    public Polycylinder createPolycylinder() {
        return new Polycylinder();
    }

    /**
     * Create an instance of {@link MappingValue }
     * 
     */
    public MappingValue createMappingValue() {
        return new MappingValue();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dl.dropbox.com/u/2416325/seeit3d_xsd", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dl.dropbox.com/u/2416325/seeit3d_xsd", name = "metricName")
    public JAXBElement<String> createMetricName(String value) {
        return new JAXBElement<String>(_MetricName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dl.dropbox.com/u/2416325/seeit3d_xsd", name = "value")
    public JAXBElement<String> createValue(String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

}