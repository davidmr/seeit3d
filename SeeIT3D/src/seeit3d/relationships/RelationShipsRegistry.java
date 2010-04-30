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
package seeit3d.relationships;

import static com.google.common.collect.Lists.*;

import java.util.Iterator;
import java.util.List;

import seeit3d.relationships.imp.CommonBaseGenerator;
import seeit3d.relationships.imp.NoRelationships;

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

	private final List<RelationShipVisualGenerator> relationshipsGenerators;

	private RelationShipsRegistry() {
		relationshipsGenerators = newArrayList(new NoRelationships(), new CommonBaseGenerator());

	}

	public Iterator<RelationShipVisualGenerator> allRelationshipsGenerator() {
		return relationshipsGenerators.iterator();
	}

}
