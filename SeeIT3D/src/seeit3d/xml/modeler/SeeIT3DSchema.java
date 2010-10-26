/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.xml.modeler;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import seeit3d.base.error.ErrorHandler;

/**
 * Represents the schema that describes the XML file. It is used to validate the structure of XML file
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DSchema {

	private static Schema schema = null;

	static {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL resource = SeeIT3DSchema.class.getResource("/seeit3d/modelers/xml/xsd/seeit3d.xsd");
			StreamSource source = new StreamSource(resource.openStream());
			schema = factory.newSchema(source);
		} catch (SAXException e) {
			ErrorHandler.error("Error while reading XML schema validation.\n Validation is disabled.");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while reading XML schema validation.\n Validation is disabled.");
			e.printStackTrace();
		}
	}

	public static Schema getSchema() {
		return schema;
	}
}
