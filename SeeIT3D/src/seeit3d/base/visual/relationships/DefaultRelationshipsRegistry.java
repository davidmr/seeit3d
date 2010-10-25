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
package seeit3d.base.visual.relationships;

import java.util.ArrayList;
import java.util.List;

import seeit3d.base.error.ErrorHandler;
import seeit3d.base.visual.api.IRelationshipsRegistry;

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
