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
package seeit3d.internal.base.model;

/**
 * Defines the properties that characterizes a visual object
 * 
 * @author David Montaño
 * 
 */
public enum VisualProperty {

	COLOR("Color", 0.0f, 0.0f), HEIGHT("Height", 0.0f, 0.0f), CROSS_SECTION("Cross Section", 0.2f, 0.1f);

	private final String formattedName;

	/**
	 * From 0 to 1
	 */
	private final float defaultValue;

	/**
	 * From 0 to 1
	 */
	private final float minValue;

	private VisualProperty(String formattedName, float defaultValue, float minValue) {
		this.formattedName = formattedName;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
	}

	public float getDefaultValue() {
		return defaultValue;
	}

	public float getMinValue() {
		return minValue;
	}

	@Override
	public String toString() {
		return formattedName;
	}
}
