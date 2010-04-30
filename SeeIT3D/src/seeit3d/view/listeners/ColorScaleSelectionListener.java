/**
 * Copyright (C) 2010  David Monta�o
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
package seeit3d.view.listeners;

import java.util.Iterator;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import seeit3d.colorscale.ColorScaleRegistry;
import seeit3d.colorscale.IColorScale;
import seeit3d.manager.SeeIT3DManager;

/**
 * This class listen for changes in color scale selection from mapping view
 * 
 * @author David Monta�o
 * 
 */
public class ColorScaleSelectionListener implements SelectionListener {

	private final SeeIT3DManager manager;

	public ColorScaleSelectionListener() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Combo combo = (Combo) event.widget;
		String colorScaleName = combo.getItem(combo.getSelectionIndex());

		Iterator<IColorScale> allColorScales = ColorScaleRegistry.getInstance().allColorScales();

		while (allColorScales.hasNext()) {
			IColorScale colorScale = allColorScales.next();
			if (colorScaleName.equals(colorScale.getName())) {
				manager.setColorScale(colorScale);
				manager.refreshVisualization();
				break;
			}
		}
	}

}
