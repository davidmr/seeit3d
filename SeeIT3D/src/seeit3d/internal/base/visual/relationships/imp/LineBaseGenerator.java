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
package seeit3d.internal.base.visual.relationships.imp;

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.vecmath.Point3f;

import seeit3d.internal.base.model.Container;

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
