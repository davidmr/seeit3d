package seeit3d.model.xml;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import seeit3d.error.ErrorHandler;

public class SeeIT3DSchema {

	private static Schema schema = null;

	static {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL resource = SeeIT3DSchema.class.getResource("/seeit3d/model/xml/xsd/seeit3d.xsd");
			StreamSource source = new StreamSource(resource.openStream());
			schema = factory.newSchema(source);
		} catch (SAXException e) {
			ErrorHandler.error("Error while reading XML schema validation.\n Validation is disabled.");
		} catch (IOException e) {
			ErrorHandler.error("Error while reading XML schema validation.\n Validation is disabled.");
		}
	}

	public static Schema getSchema() {
		return schema;
	}
}
