//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.14 at 06:31:01 PM COT 
//


package seeit3d.modelers.xml.internal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}metricsList"/>
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}mapping" minOccurs="0"/>
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}polycylinder" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="granularityLevelName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="related" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
 *       &lt;attribute name="visible" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "metricsList",
    "mapping",
    "polycylinder"
})
@XmlRootElement(name = "container")
public class Container {

    @XmlElement(required = true)
    protected MetricsList metricsList;
    protected Mapping mapping;
    @XmlElement(required = true)
    protected List<Polycylinder> polycylinder;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String name;
    @XmlAttribute(required = true)
    protected String granularityLevelName;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> related;
    @XmlAttribute(required = true)
    protected boolean visible;

    /**
     * Gets the value of the metricsList property.
     * 
     * @return
     *     possible object is
     *     {@link MetricsList }
     *     
     */
    public MetricsList getMetricsList() {
        return metricsList;
    }

    /**
     * Sets the value of the metricsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetricsList }
     *     
     */
    public void setMetricsList(MetricsList value) {
        this.metricsList = value;
    }

    /**
     * Gets the value of the mapping property.
     * 
     * @return
     *     possible object is
     *     {@link Mapping }
     *     
     */
    public Mapping getMapping() {
        return mapping;
    }

    /**
     * Sets the value of the mapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mapping }
     *     
     */
    public void setMapping(Mapping value) {
        this.mapping = value;
    }

    /**
     * Gets the value of the polycylinder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the polycylinder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolycylinder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Polycylinder }
     * 
     * 
     */
    public List<Polycylinder> getPolycylinder() {
        if (polycylinder == null) {
            polycylinder = new ArrayList<Polycylinder>();
        }
        return this.polycylinder;
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
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the granularityLevelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGranularityLevelName() {
        return granularityLevelName;
    }

    /**
     * Sets the value of the granularityLevelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGranularityLevelName(String value) {
        this.granularityLevelName = value;
    }

    /**
     * Gets the value of the related property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the related property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelated().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getRelated() {
        if (related == null) {
            related = new ArrayList<Object>();
        }
        return this.related;
    }

    /**
     * Gets the value of the visible property.
     * 
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the value of the visible property.
     * 
     */
    public void setVisible(boolean value) {
        this.visible = value;
    }

}
