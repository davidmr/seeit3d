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
package seeit3d.base.ui.ide.view.listeners;

import static seeit3d.base.bus.EventBus.*;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import seeit3d.base.SeeIT3D;
import seeit3d.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.ui.actions.ApplyChangeRelationShipGenerator;
import seeit3d.base.visual.api.IRelationshipsRegistry;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

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
