//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.17 at 03:03:40 PM COT 
//

package seeit3d.modelers.xml.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element ref="{http://dl.dropbox.com/u/2416325/seeit3d_xsd}mappingValue" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "mappingValue" })
@XmlRootElement(name = "mapping")
public class Mapping implements Serializable{

	private static final long serialVersionUID = 426643325388612643L;

	@XmlElement(required = true)
	protected List<MappingValue> mappingValue;

	/**
	 * Gets the value of the mappingValue property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the mappingValue property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMappingValue().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link MappingValue }
	 * 
	 * 
	 */
	public List<MappingValue> getMappingValue() {
		if (mappingValue == null) {
			mappingValue = new ArrayList<MappingValue>();
		}
		return this.mappingValue;
	}

}