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
package seeit3d.utils;

import javax.vecmath.Color3f;

import seeit3d.general.model.VisualProperty;

/**
 * Constants in the view (Colors, and sizes)
 * 
 * @author David
 * 
 */
public class ViewConstants {

	public static final String VISUALIZATION_EXTENSION = "s3d";

	public static final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);

	public static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

	public static final Color3f RED = new Color3f(1.0f, 0.0f, 0.0f);

	public static final Color3f GREEN = new Color3f(0.0f, 1.0f, 0.0f);

	public static final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);

	public static final float PICKING_TOLERANCE = 0.0f;

	public static final float POLYCYLINDER_SPACING = 0.15f;

	public static final float CONTAINERS_SPACING = 2.0f;

	public static final float HIGHLIGHT_PADDING = 0.1f;

	public static final VisualProperty DEFAULT_SORTING_PROPERTY = VisualProperty.HEIGHT;

}
