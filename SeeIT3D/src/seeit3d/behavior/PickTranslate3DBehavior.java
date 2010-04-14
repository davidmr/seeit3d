package seeit3d.behavior;

import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.pickfast.PickTool;
import com.sun.j3d.utils.pickfast.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * Based on PickTranslateBehavior
 * 
 * @author david
 * 
 */
public class PickTranslate3DBehavior extends PickMouseBehavior {

	private final MouseTranslate3D translate;

	public PickTranslate3DBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds, ViewingPlatform viewingPlatform) {
		super(canvas, root, bounds);
		translate = new MouseTranslate3D(MouseBehavior.MANUAL_WAKEUP, viewingPlatform);
		translate.setTransformGroup(currGrp);
		currGrp.addChild(translate);
		translate.setSchedulingBounds(bounds);
		this.setSchedulingBounds(bounds);
	}

	@Override
	public void updateScene(int xpos, int ypos) {
		pickCanvas.setShapeLocation(xpos, ypos);

		pickCanvas.setFlags(PickInfo.NODE | PickInfo.SCENEGRAPHPATH);
		PickInfo pickInfo = pickCanvas.pickClosest();

		if (pickInfo != null) {
			TransformGroup tg = (TransformGroup) pickCanvas.getNode(pickInfo, PickTool.TYPE_TRANSFORM_GROUP);
			if ((tg != null) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
				translate.setTransformGroup(tg);
				translate.wakeup();
			}
		}
	}

}
