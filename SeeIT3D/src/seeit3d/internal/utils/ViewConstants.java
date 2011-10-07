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
package seeit3d.internal.utils;

import java.awt.BasicStroke;

import javax.vecmath.Color3f;

import seeit3d.internal.base.model.VisualProperty;

/**
 * Constants in the view (Colors, and sizes)
 * 
 * @author David Montaño
 * 
 */
public class ViewConstants {

	public static final String VISUALIZATION_EXTENSION = "s3d";

	public static final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);

	public static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

	public static final Color3f RED = new Color3f(1.0f, 0.0f, 0.0f);

	public static final Color3f GREEN = new Color3f(0.0f, 1.0f, 0.0f);

	public static final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);

	public static final Color3f YELLOW = new Color3f(1.0f, 1.0f, 0.0f);

	public static final float PICKING_TOLERANCE = 0.0f;

	public static final int SELECTION_TOOL_STEP = 5;

	public static final float POLYCYLINDER_SPACING = 0.15f;

	public static final float CONTAINERS_SPACING = 2.0f;

	public static final float HIGHLIGHT_PADDING = 0.1f;

	public static final VisualProperty DEFAULT_SORTING_PROPERTY = VisualProperty.HEIGHT;
	
	public static final BasicStroke SELECTION_STROKE = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{3f}, 0.0f);


}
