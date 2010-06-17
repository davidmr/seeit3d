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
package seeit3d.general;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.core.handler.SeeIT3DCoreHandler;
import seeit3d.general.model.Preferences;
import seeit3d.ui.handler.SeeIT3DUIHandler;
import seeit3d.visual.api.SeeIT3DVisualProperties;
import seeit3d.visual.handler.SeeIT3DVisualPropertiesHandler;

/**
 * Class use as an access point to the different API modules
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DAPILocator {

	private static final SeeIT3DCore core;

	private static final SeeIT3DVisualProperties visual;

	private static final SeeIT3DUIHandler ui;

	static {
		core = new SeeIT3DCoreHandler();
		visual = new SeeIT3DVisualPropertiesHandler();
		ui = new SeeIT3DUIHandler();
	}

	public static SeeIT3DCore findCore() {
		return core;
	}

	public static SeeIT3DVisualProperties findVisualProperties() {
		return visual;
	}

	public static SeeIT3DUIHandler findUi() {
		return ui;
	}

	public static Preferences findPreferences() {
		return Preferences.getInstance();
	}
}
