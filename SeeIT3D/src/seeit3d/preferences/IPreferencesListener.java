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
package seeit3d.preferences;

import javax.vecmath.Color3f;

import seeit3d.colorscale.IColorScale;

/**
 * Defines a preferences listener that will be notified when a preference change
 * 
 * @author David Montaño
 * 
 */
public interface IPreferencesListener {

	public void colorScaleChanged(IColorScale newColorScale);

	public void scaleStepChanged(double newScale);

	public void containersPerRowChanged(int newContainersPerRow);

	public void backgroundColorChanged(Color3f newBackgroundColor);

	public void polycylindersPerRowChanged(int newPolycylinderPerRow);

	public void highlightColorChanged(Color3f newHighlightColor);

	public void relationMarkColorChanged(Color3f newRelationMarkColor);

	public void transparencyStepChanged(float transparencyStepChanged);

}
