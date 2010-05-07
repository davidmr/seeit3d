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
package seeit3d.view.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.relationships.RelationShipVisualGenerator;
import seeit3d.relationships.RelationShipsRegistry;

/**
 * Listener for relationships generator selection in the mapping view
 * 
 * @author David Montaño
 * 
 */
public class RelationshipSelectionListener implements SelectionListener {

	private final SeeIT3DManager manager;

	public RelationshipSelectionListener() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Combo combo = (Combo) event.widget;
		String relationshipGeneratorName = combo.getItem(combo.getSelectionIndex());

		Iterable<RelationShipVisualGenerator> allRelationshipsGenerator = RelationShipsRegistry.getInstance().allRelationshipsGenerator();
		for (RelationShipVisualGenerator generator : allRelationshipsGenerator) {
			if (generator.getName().equals(relationshipGeneratorName)) {
				manager.setRelationShipVisualGenerator(generator);
				manager.refreshVisualization();
				break;
			}
		}

	}

}
