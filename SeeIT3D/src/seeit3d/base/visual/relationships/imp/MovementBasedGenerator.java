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

import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import seeit3d.base.World;
import seeit3d.base.model.Container;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

/**
 * Implementation of <code>ISceneGraphRelationshipGenerator</code> that uses movement to show the relationships. It changes the apparent size of the related containers
 * 
 * @author David Montaño
 * 
 */
public class MovementBasedGenerator implements ISceneGraphRelationshipGenerator {

	private static final String NAME = "Movement";

	@Override
	public void initialize() {}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer, Color3f relationshipColor) {
		List<Container> relatedContainers = baseContainer.getRelatedContainers();
		addMovementBehavior(baseContainer);
		for (Container container : relatedContainers) {
			addMovementBehavior(container);
		}
		return relatedContainers;
	}

	private void addMovementBehavior(Container container) {

		Alpha alpha = new Alpha();
		alpha.setLoopCount(-1);
		alpha.setMode(Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE);
		alpha.setIncreasingAlphaDuration(200);
		alpha.setIncreasingAlphaRampDuration(0);
		alpha.setAlphaAtOneDuration(0);
		alpha.setDecreasingAlphaDuration(200);
		alpha.setDecreasingAlphaRampDuration(0);
		alpha.setAlphaAtZeroDuration(0);

		// reparenting process
		BranchGroup containerBG = container.getContainerBG();
		TransformGroup transformGroup = container.getTransformGroup();

		containerBG.removeChild(transformGroup);

		TransformGroup target = new TransformGroup();
		target.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		target.addChild(transformGroup);

		containerBG.addChild(target);

		ScaleInterpolator interpolator = new ScaleInterpolator(alpha, target, new Transform3D(), 0.98f, 1.0f);

		interpolator.setSchedulingBounds(World.bounds);
		container.getContainerBG().addChild(interpolator);

	}

	@Override
	public void unused() {}

	@Override
	public String getName() {
		return NAME;
	}

}
