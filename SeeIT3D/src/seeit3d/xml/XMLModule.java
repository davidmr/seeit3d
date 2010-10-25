package seeit3d.xml;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.xml.modeler.XMLBasedModelGenerator;
import seeit3d.xml.modeler.annotation.XMLModeler;
import seeit3d.xml.modeler.internal.Container;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class XMLModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(new TypeLiteral<IModelGenerator<Container>>() {}).annotatedWith(XMLModeler.class).to(XMLBasedModelGenerator.class);

	}

}
