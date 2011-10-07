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

/**
 * 
 * Class to represent a child in analysis process. For example a Class has Methods as children
 * 
 * @author David Montaño
 * 
 */
public class Child {

	private final String name;

	private final Object object;

	private final IEclipseResourceRepresentation representation;

	public Child(String name, Object object) {
		this(name, object, null);
	}

	public Child(String name, Object object, IEclipseResourceRepresentation representation) {
		this.name = name;
		this.object = object;
		this.representation = representation;
	}

	public String getName() {
		return name;
	}

	public Object getObject() {
		return object;
	}

	public IEclipseResourceRepresentation getRepresentation() {
		return representation;
	}

}
