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
package seeit3d.analysis;

import org.eclipse.core.resources.IResource;

/**
 * Class that represents an object who has not a representation in Eclipse
 * 
 * @author David Montaño
 * 
 */
public class NoEclipseResourceRepresentation implements IEclipseResourceRepresentation {

	public static final NoEclipseResourceRepresentation INSTANCE = new NoEclipseResourceRepresentation();

	private NoEclipseResourceRepresentation() {}

	@Override
	public IResource getResource() {
		return null;
	}

	@Override
	public Object getEclipseSeletable() {
		return null;
	}

}
