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

package seeit3d.modelers.xml;

import seeit3d.general.model.IContainerRepresentedObject;
import seeit3d.modelers.xml.internal.Container;

/**
 * Indicates the representation of a container that is specified in a XML file
 * 
 * @author David Montaño
 * 
 */
public class XMLBasedBasicRepresentation implements IContainerRepresentedObject {

	private static final long serialVersionUID = -5819100372296201970L;

	private final Container container;

	public XMLBasedBasicRepresentation(Container container) {
		this.container = container;
	}

	@Override
	public String getName() {
		return container.getName();
	}

	@Override
	public String granularityLevelName(int countLevelsDown) {
		return container.getGranularityLevelName();
	}

}
