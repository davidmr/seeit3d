package seeit3d.visual.relationships.imp;

import java.util.List;

import javax.media.j3d.*;

import seeit3d.core.handler.SceneGraphHandler;
import seeit3d.general.model.Container;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

public class MovementBasedGenerator implements ISceneGraphRelationshipGenerator {

	private static final String NAME = "Movement";

	@Override
	public void initialize() {}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {
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

		interpolator.setSchedulingBounds(SceneGraphHandler.bounds);
		container.getContainerBG().addChild(interpolator);

	}

	@Override
	public void unused() {}

	@Override
	public String getName() {
		return NAME;
	}

}
