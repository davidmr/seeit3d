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
package seeit3d.internal.base.ui.ide.view.listeners;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.bus.events.ChangeColorScaleEvent;
import seeit3d.internal.base.visual.api.IColorScaleRegistry;
import seeit3d.internal.base.visual.colorscale.IColorScale;

import com.google.inject.Inject;

/**
 * This class listen for changes in color scale selection from mapping view
 * 
 * @author David Montaño
 * 
 */
public class ColorScaleSelectionListener implements SelectionListener {

	private IColorScaleRegistry colorScaleRegistry;

	public ColorScaleSelectionListener() {
		SeeIT3D.injector().injectMembers(this);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Combo combo = (Combo) event.widget;
		String colorScaleName = combo.getItem(combo.getSelectionIndex());

		Iterable<IColorScale> allColorScales = colorScaleRegistry.allColorScales();

		for (IColorScale colorScale : allColorScales) {
			if (colorScaleName.equals(colorScale.getName())) {
				publishEvent(new ChangeColorScaleEvent(colorScale));
				break;
			}
		}
	}

	@Inject
	public void setColorScaleRegistry(IColorScaleRegistry colorScaleRegistry) {
		this.colorScaleRegistry = colorScaleRegistry;
	}

}
