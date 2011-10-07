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
package seeit3d.internal.base.visual.handler;

import static seeit3d.internal.base.bus.EventBus.publishEvent;
import static seeit3d.internal.base.bus.EventBus.registerListener;
import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.ChangeColorScaleEvent;
import seeit3d.internal.base.bus.events.ColorScaleChangedEvent;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.visual.api.ISeeIT3DVisualProperties;
import seeit3d.internal.base.visual.colorscale.IColorScale;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class to handle the visual properties module general state
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultSeeIT3DVisualProperties implements ISeeIT3DVisualProperties, IEventListener {

	private IColorScale colorScale;

	@Inject
	public DefaultSeeIT3DVisualProperties(ISeeIT3DPreferences preferences) {
		colorScale = preferences.getColorScale();
		registerListener(ChangeColorScaleEvent.class, this);
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof ChangeColorScaleEvent) {
			setColorScale(((ChangeColorScaleEvent) event).getColorScale());
			publishEvent(new ColorScaleChangedEvent());
		}
	}

	private void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	@Override
	public IColorScale getCurrentColorScale() {
		return colorScale;
	}

}
