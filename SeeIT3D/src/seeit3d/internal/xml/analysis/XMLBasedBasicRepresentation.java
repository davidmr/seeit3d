/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.xml.analysis;

import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.internal.xml.internal.Container;

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

	@Override
	public String toString() {
		return getName();
	}

}
