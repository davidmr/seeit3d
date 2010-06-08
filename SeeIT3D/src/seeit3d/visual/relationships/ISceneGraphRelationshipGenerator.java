/**
 * Copyright (C) 2010  David Monta�o
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

import java.util.List;

import seeit3d.general.model.Container;

/**
 * Interface representing a visualization generator for relationships
 * 
 * @author David Monta�o
 * 
 */
public interface ISceneGraphRelationshipGenerator {

	public List<Container> generateVisualRelationShips(Container baseContainer);

	public String getName();

}
