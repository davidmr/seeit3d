//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.17 at 03:03:40 PM COT 
//

package seeit3d.model.xml.internal;

import java.io.Serializable;

import javax.xml.bind.annotation.*;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}name"/>
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}metricsValue"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "metricsValue" })
@XmlRootElement(name = "polycylinder")
public class Polycylinder implements Serializable{

	private static final long serialVersionUID = -5216165642861331236L;

	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected MetricsValue metricsValue;

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the metricsValue property.
	 * 
	 * @return possible object is {@link MetricsValue }
	 * 
	 */
	public MetricsValue getMetricsValue() {
		return metricsValue;
	}

	/**
	 * Sets the value of the metricsValue property.
	 * 
	 * @param value
	 *            allowed object is {@link MetricsValue }
	 * 
	 */
	public void setMetricsValue(MetricsValue value) {
		this.metricsValue = value;
	}

}
