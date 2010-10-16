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
package seeit3d.base.visual.relationships.imp;

import java.util.ArrayList;
import java.util.List;

import seeit3d.base.model.Container;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

/**
 * Null relationship generator. It generates nothing
 * 
 * @author David Montaño
 * 
 */
public class NoRelationships implements ISceneGraphRelationshipGenerator {

	private static final String NAME = "No Visual Relation";

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {
		return new ArrayList<Container>();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void unused() {

	}

}
