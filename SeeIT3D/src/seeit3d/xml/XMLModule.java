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
package seeit3d.xml;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.xml.modeler.XMLBasedModelGenerator;
import seeit3d.xml.modeler.annotation.XMLModeler;
import seeit3d.xml.modeler.internal.Container;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * Guice module for dependency injection within the XML analysis
 * 
 * @author David Montaño
 * 
 */
public class XMLModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(new TypeLiteral<IModelGenerator<Container>>() {}).annotatedWith(XMLModeler.class).to(XMLBasedModelGenerator.class);

	}

}
