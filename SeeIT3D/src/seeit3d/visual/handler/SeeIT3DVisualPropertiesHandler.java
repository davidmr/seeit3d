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
package seeit3d.visual.handler;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.ChangeColorScaleEvent;
import seeit3d.general.model.Preferences;
import seeit3d.visual.api.SeeIT3DVisualProperties;
import seeit3d.visual.colorscale.IColorScale;

/**
 * Class to handle the visual properties module general state
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DVisualPropertiesHandler implements SeeIT3DVisualProperties, IEventListener {

	private IColorScale colorScale;

	public SeeIT3DVisualPropertiesHandler() {
		colorScale = Preferences.getInstance().getColorScale();
		registerListener(ChangeColorScaleEvent.class, this);
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof ChangeColorScaleEvent) {
			setColorScale(((ChangeColorScaleEvent) event).getColorScale());
		}

	}

	private void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	@Override
	public IColorScale getColorScale() {
		return colorScale;
	}

}
