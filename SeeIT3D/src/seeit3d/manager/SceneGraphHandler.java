package seeit3d.manager;

import java.util.Enumeration;
import java.util.Iterator;

import javax.media.j3d.*;
import javax.vecmath.*;

import seeit3d.behavior.*;
import seeit3d.error.exception.SeeIT3DException;
import seeit3d.model.representation.Container;
import seeit3d.preferences.Preferences;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;
import seeit3d.view.SeeIT3DCanvas;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class SceneGraphHandler {

	private final SeeIT3DManager manager;

	private final Preferences preferences;

	private SeeIT3DCanvas canvas = null;

	private BranchGroup rootObj = null;

	private SimpleUniverse universe = null;

	private Bounds bounds = null;

	private BranchGroup containersGroup = null;

	private OrbitBehavior orbit = null;

	public SceneGraphHandler(SeeIT3DManager manager, Preferences preferences) {
		this.manager = manager;
		this.preferences = preferences;
	}

	public void addChildToGroup(BranchGroup branchGroup) {
		containersGroup.addChild(branchGroup);
	}

	@SuppressWarnings("unchecked")
	public void clearScene() {
		Enumeration<Node> children = containersGroup.getAllChildren();
		while (children.hasMoreElements()) {
			Node node = children.nextElement();
			if (node instanceof BranchGroup) {
				((BranchGroup) node).detach();
				containersGroup.removeChild(node);
			}
		}
	}

	public synchronized void enableOrbiting() {
		changeOrbitState(true);
	}

	public synchronized void disableOrbiting() {
		changeOrbitState(false);
	}

	private synchronized void changeOrbitState(boolean enabled) {
		if (orbit != null) {
			orbit.setRotateEnable(enabled);
			orbit.setTranslateEnable(enabled);
		}
	}

	public synchronized void initializeVisualization(SeeIT3DCanvas canvasToSet) {
		canvas = canvasToSet;
		universe = new SimpleUniverse(canvas);

		canvas.getView().setFrontClipPolicy(View.VIRTUAL_EYE);
		canvas.getView().setBackClipPolicy(View.VIRTUAL_EYE);
		canvas.getView().setFrontClipDistance(0.1f);
		canvas.getView().setBackClipDistance(100.0f);

		buildAllVisualization();

	}

	public synchronized void buildAllVisualization() {
		if (rootObj != null) {
			universe.getLocale().removeBranchGraph(rootObj);
		}

		rootObj = new BranchGroup();
		rootObj.setCapability(BranchGroup.ALLOW_DETACH);
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		buildWorld(canvas);
		buildGraph();
		setViewersPosition(20);
		Utils.buildAxis(rootObj);

		rootObj.compile();
		universe.addBranchGraph(rootObj);
	}

	public synchronized void removeFromSceneGraph(Container container) {
		BranchGroup containerBG = container.getContainerBG();
		containerBG.detach();
		containersGroup.removeChild(containerBG);
	}

	private synchronized void buildWorld(Canvas3D canvas) {

		ViewingPlatform viewingPlatform = universe.getViewingPlatform();

		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 2000.0f);
		rootObj.setBounds(bounds);

		/************* LIGHT ****************/
		AmbientLight ambientLightNode = new AmbientLight(ViewConstants.BLACK);
		ambientLightNode.setInfluencingBounds(bounds);
		rootObj.addChild(ambientLightNode);

		DirectionalLight dLight = new DirectionalLight(true, ViewConstants.WHITE, new Vector3f(-1.0f, 1.0f, 1.0f));
		dLight.setInfluencingBounds(bounds);
		rootObj.addChild(dLight);

		DirectionalLight dLight1 = new DirectionalLight(true, ViewConstants.WHITE, new Vector3f(1.0f, 1.0f, 1.0f));
		dLight1.setInfluencingBounds(bounds);
		rootObj.addChild(dLight1);

		DirectionalLight dLight2 = new DirectionalLight(true, ViewConstants.WHITE, new Vector3f(-1.0f, 1.0f, -1.0f));
		dLight2.setInfluencingBounds(bounds);
		rootObj.addChild(dLight2);

		DirectionalLight dLight3 = new DirectionalLight(true, ViewConstants.WHITE, new Vector3f(1.0f, 1.0f, -1.0f));
		dLight3.setInfluencingBounds(bounds);
		rootObj.addChild(dLight3);

		DirectionalLight dLight4 = new DirectionalLight(true, ViewConstants.WHITE, new Vector3f(0.0f, -1.0f, 0.0f));
		dLight4.setInfluencingBounds(bounds);
		rootObj.addChild(dLight4);

		/************* BACKGROUND ****************/
		Background back = new Background(preferences.getBackgroundColor());
		back.setApplicationBounds(bounds);
		rootObj.addChild(back);

		/******* BEHAVIORS ********/

		PickRotate3DBehavior rotate = new PickRotate3DBehavior(canvas, rootObj, bounds, viewingPlatform);
		rotate.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(rotate);

		PickMouseBehavior selectionBehavior = new MouseClickedBehavior(canvas, rootObj, bounds);
		selectionBehavior.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(selectionBehavior);

		PickTranslate3DBehavior translation = new PickTranslate3DBehavior(canvas, rootObj, bounds, viewingPlatform);
		translation.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(translation);

		orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ROTATE | OrbitBehavior.REVERSE_TRANSLATE | OrbitBehavior.STOP_ZOOM);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);

	}

	private synchronized void buildGraph() throws SeeIT3DException {
		Iterator<Container> iterator = manager.iteratorOnAllContainers();

		containersGroup = new BranchGroup();
		containersGroup.setCapability(BranchGroup.ALLOW_DETACH);
		containersGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		containersGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		containersGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		TransformGroup containersTG = new TransformGroup();
		containersTG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		containersTG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		containersTG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		containersGroup.addChild(containersTG);

		Container container = null;
		while (iterator.hasNext()) {
			container = iterator.next();
			container.updateVisualRepresentation();
			BranchGroup bgContainer = container.getContainerBG();
			containersTG.addChild(bgContainer);
		}
		rootObj.addChild(containersGroup);

	}

	public synchronized void rebuildSceneGraph() {
		if (containersGroup != null) {
			containersGroup.detach();
			rootObj.removeChild(containersGroup);
			buildGraph();
		}
	}

	public TransformGroup getViewPlatformTransform() {
		return universe.getViewingPlatform().getViewPlatformTransform();
	}

	public void setViewersPosition(float max) {
		float xMax = Math.max(max, 5);
		ViewingPlatform vp = universe.getViewingPlatform();
		TransformGroup tg = vp.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		tg.getTransform(t3d);
		t3d.lookAt(new Point3d(xMax * 2, xMax, xMax * 2), new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
		t3d.invert();
		tg.setTransform(t3d);
	}

}
