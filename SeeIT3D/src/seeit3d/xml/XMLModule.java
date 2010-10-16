package seeit3d.xml;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.xml.modeler.XMLBasedModelGenerator;
import seeit3d.xml.modeler.annotation.XMLModeler;

import com.google.inject.AbstractModule;

public class XMLModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(IModelGenerator.class).annotatedWith(XMLModeler.class).to(XMLBasedModelGenerator.class);

	}

}
