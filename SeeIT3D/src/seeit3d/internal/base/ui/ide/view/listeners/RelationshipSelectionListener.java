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

import seeit3d.internal.SeeIT3D;
import seeit3d.internal.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.ui.actions.ApplyChangeRelationShipGenerator;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

import com.google.inject.Inject;

/**
 * Listener for relationships generator selection in the mapping view
 * 
 * @author David Montaño
 * 
 */
public class RelationshipSelectionListener implements SelectionListener {

	private IRelationshipsRegistry relationshipsRegistry;

	public RelationshipSelectionListener() {
		SeeIT3D.injector().injectMembers(this);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {

		Combo combo = (Combo) event.widget;

		String selectedGeneratorName = combo.getItem(combo.getSelectionIndex());
		Iterable<Class<? extends ISceneGraphRelationshipGenerator>> allRelationshipsGenerator = relationshipsRegistry.allRelationshipsGenerator();
		for (Class<? extends ISceneGraphRelationshipGenerator> generator : allRelationshipsGenerator) {
			String relationName = relationshipsRegistry.getRelationName(generator);
			if (relationName.equals(selectedGeneratorName)) {
				PerformOperationOnSelectedContainersEvent operation = createEvent(generator);
				publishEvent(operation);
				break;
			}
		}

	}

	private PerformOperationOnSelectedContainersEvent createEvent(final Class<? extends ISceneGraphRelationshipGenerator> generatorClass) {
		FunctionToApplyOnContainer function = new ApplyChangeRelationShipGenerator(generatorClass);
		return new PerformOperationOnSelectedContainersEvent(function, true);
	}

	@Inject
	public void setRelationshipsRegistry(IRelationshipsRegistry relationshipsRegistry) {
		this.relationshipsRegistry = relationshipsRegistry;
	}
}
