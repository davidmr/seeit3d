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
package seeit3d.ui.ide.view.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.general.SeeIT3DAPILocator;

/**
 * Listener for the checkbox in mapping view to update if the related containers are going to be added to the view
 * 
 * @author David Montaño
 * 
 */
public class ShowRelatedListener implements SelectionListener {

	private final SeeIT3DCore core;

	public ShowRelatedListener() {
		core = SeeIT3DAPILocator.findCore();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		Button check = (Button) e.widget;
		boolean checked = check.getSelection();
		core.setShowRelatedContainers(checked);
		core.refreshVisualization();
	}

}
