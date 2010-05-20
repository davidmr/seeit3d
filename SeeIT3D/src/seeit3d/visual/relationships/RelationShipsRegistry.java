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
package seeit3d.visual.relationships;

import java.util.ArrayList;
import java.util.List;

import seeit3d.core.handler.error.ErrorHandler;
import seeit3d.visual.relationships.imp.*;

/**
 * General registry of all relationships visualization generators
 * 
 * @author David Montaño
 * 
 */
public class RelationShipsRegistry {

	private static final RelationShipsRegistry instance = new RelationShipsRegistry();

	public static RelationShipsRegistry getInstance() {
		return instance;
	}

	private final List<Class<? extends IRelationShipVisualGenerator>> relationshipsGenerators;

	private RelationShipsRegistry() {
		relationshipsGenerators = new ArrayList<Class<? extends IRelationShipVisualGenerator>>();
		relationshipsGenerators.add(NoRelationships.class);
		relationshipsGenerators.add(CommonBaseGenerator.class);
		relationshipsGenerators.add(LineBaseGenerator.class);

	}

	public Iterable<Class<? extends IRelationShipVisualGenerator>> allRelationshipsGenerator() {
		return relationshipsGenerators;
	}

	public String getRelationName(Class<? extends IRelationShipVisualGenerator> selectedGenerator) {
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

	public IRelationShipVisualGenerator createNewInstance(Class<? extends IRelationShipVisualGenerator> generator) {
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
