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

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.events.ChangeGranularityLevelEvent;

/**
 * Listener for change on level of granularity selected
 * 
 * @author David Montaño
 * 
 */
public class SelectionComponentListener implements SelectionListener {

	public static final String COMPONENT_LEVEL_DETAIL = "component_level_detail";

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Button source = (Button) event.getSource();
		ChangeLevelOption option = (ChangeLevelOption) source.getData(COMPONENT_LEVEL_DETAIL);
		publishEvent(new ChangeGranularityLevelEvent(option.moreDetail));
	}
}
