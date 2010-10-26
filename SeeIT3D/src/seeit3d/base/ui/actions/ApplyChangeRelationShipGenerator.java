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
package seeit3d.base.ui.actions;

import seeit3d.base.SeeIT3D;
import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;
import seeit3d.base.visual.api.IRelationshipsRegistry;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

import com.google.inject.Inject;

/**
 * This class applies a specific relatioship generator to the containers
 * 
 * @author David Montaño
 * 
 */
public final class ApplyChangeRelationShipGenerator extends FunctionToApplyOnContainer {

	private final Class<? extends ISceneGraphRelationshipGenerator> generatorClass;

	private IRelationshipsRegistry relationshipsRegistry;

	public ApplyChangeRelationShipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generatorClass) {
		SeeIT3D.injector().injectMembers(this);
		this.generatorClass = generatorClass;
	}

	@Override
	public Container apply(Container container) {
		ISceneGraphRelationshipGenerator generator = relationshipsRegistry.createNewInstance(generatorClass);
		container.setSceneGraphRelationshipGenerator(generator);
		return container;
	}

	@Inject
	public void setRelationshipsRegistry(IRelationshipsRegistry relationshipsRegistry) {
		this.relationshipsRegistry = relationshipsRegistry;
	}
}