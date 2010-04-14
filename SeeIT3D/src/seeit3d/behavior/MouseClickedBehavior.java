package seeit3d.behavior;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.TransformGroup;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.model.representation.Container;
import seeit3d.model.representation.PolyCylinder;
import seeit3d.utils.ViewConstants;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

/**
 * This class is called when an object in the scene is selected
 * 
 * @author David
 * 
 */
public class MouseClickedBehavior extends PickMouseBehavior {

	private final SeeIT3DManager manager;

	public MouseClickedBehavior(Canvas3D canvas, BranchGroup rootBg, Bounds bounds) {
		super(canvas, rootBg, bounds);
		setSchedulingBounds(bounds);
		pickCanvas.setTolerance(ViewConstants.PICKING_TOLERANCE);
		pickCanvas.setMode(PickTool.GEOMETRY);
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void updateScene(int xPos, int yPos) {

		PickResult pickResult = null;

		Container selectedContainer = null;
		PolyCylinder selectedPolyCylinder = null;

		pickCanvas.setShapeLocation(xPos, yPos);
		pickResult = pickCanvas.pickClosest();
		if (pickResult != null) {
			selectedContainer = findContainerAssociated(pickResult);
			selectedPolyCylinder = findPolyCylinderAssociated(pickResult);
		}

		boolean toggleContainerSelection = mevent.isControlDown();
		boolean togglePolycylinderSelection = mevent.isShiftDown();
		manager.changeSelectionAndUpdateMappingView(selectedContainer, selectedPolyCylinder, toggleContainerSelection, togglePolycylinderSelection);
	}

	private Container findContainerAssociated(PickResult pickResult) {
		TransformGroup containerTransformGroup = (TransformGroup) pickResult.getNode(PickResult.TRANSFORM_GROUP);
		if (containerTransformGroup != null) {
			Node branchGroupParent = containerTransformGroup.getParent();
			return (Container) branchGroupParent.getUserData();
		}
		return null;
	}

	private PolyCylinder findPolyCylinderAssociated(PickResult pickResult) {
		SceneGraphPath sceneGraphPath = pickResult.getSceneGraphPath();
		for (int i = 0; i < sceneGraphPath.nodeCount(); i++) {
			Node node = sceneGraphPath.getNode(i);
			if (node instanceof Box) {
				return (PolyCylinder) node.getUserData();
			}
		}
		return null;
	}

}
