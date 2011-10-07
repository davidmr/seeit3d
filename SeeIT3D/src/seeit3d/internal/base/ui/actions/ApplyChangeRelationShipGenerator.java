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
package seeit3d.internal.base.ui.actions;

import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

import com.google.inject.Inject;

/**
 * This class applies a specific relationship generator to the containers
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