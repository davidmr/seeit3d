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

package seeit3d.general.model;

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
