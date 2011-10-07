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

import java.util.List;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ScaleInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import seeit3d.internal.base.World;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

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
