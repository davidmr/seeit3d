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

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.vecmath.Point3f;

import seeit3d.base.model.Container;

/**
 * Relationship generator based on lines to connect each related elements
 * 
 * @author David Montaño
 * 
 */
public class LineBaseGenerator extends GeometryBasedGenerator {

	private static final String NAME = "Lines";

	@Override
	public Geometry createGeometryBetweenContainers(Container origin, Container destination) {
		Point3f originPoint = extractPointFromVector(origin.getPosition());
		Point3f destinationPoint = extractPointFromVector(destination.getPosition());

		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinate(0, originPoint);
		line.setCoordinate(1, destinationPoint);
		return line;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
