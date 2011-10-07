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
import org.eclipse.swt.widgets.Button;

import seeit3d.internal.base.bus.events.ChangeGranularityLevelEvent;

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
