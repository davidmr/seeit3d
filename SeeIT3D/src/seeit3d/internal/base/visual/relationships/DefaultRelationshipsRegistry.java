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
package seeit3d.internal.base.visual.relationships;

import java.util.ArrayList;
import java.util.List;

import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;

import com.google.inject.Singleton;

/**
 * General registry of all relationships visualization generators
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultRelationshipsRegistry implements IRelationshipsRegistry {

	private final List<Class<? extends ISceneGraphRelationshipGenerator>> relationshipsGenerators;

	public DefaultRelationshipsRegistry() {
		relationshipsGenerators = new ArrayList<Class<? extends ISceneGraphRelationshipGenerator>>();
	}

	@Override
	public void registerRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generator) {
		relationshipsGenerators.add(generator);
	}

	@Override
	public Iterable<Class<? extends ISceneGraphRelationshipGenerator>> allRelationshipsGenerator() {
		return relationshipsGenerators;
	}

	@Override
	public String getRelationName(Class<? extends ISceneGraphRelationshipGenerator> selectedGenerator) {
		try {
			return selectedGenerator.newInstance().getName();
		} catch (InstantiationException e) {
			ErrorHandler.error("Error while getting relationships name");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			ErrorHandler.error("Error while getting relationships name");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ISceneGraphRelationshipGenerator createNewInstance(Class<? extends ISceneGraphRelationshipGenerator> generator) {
		try {
			return generator.newInstance();
		} catch (InstantiationException e) {
			ErrorHandler.error("Error while creating new instance of generator");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			ErrorHandler.error("Error while creating new instance of generator");
			e.printStackTrace();
		}
		return null;
	}

}
