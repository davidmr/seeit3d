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
package seeit3d.core.handler;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.*;

import seeit3d.general.error.exception.SeeIT3DException;
import seeit3d.general.model.Container;
import seeit3d.general.model.Preferences;
import seeit3d.ui.behavior.MouseClickedBehavior;
import seeit3d.ui.behavior.PickRotate3DBehavior;
import seeit3d.ui.behavior.PickTranslate3DBehavior;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.behaviors.PickingCallback;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * Class that handles all the interactions with the scene graph in Java 3D. All the changes in general structure of the scene graph should be handle by this class.
 * 
 * @author David Montaño
 * 
 */
public class SceneGraphHandler {

	private final SeeIT3DCoreHandler manager;

	private SeeIT3DCanvas canvas = null;

	private BranchGroup rootObj = null;

	private SimpleUniverse universe = null;

	public static Bounds bounds = null;

	private BranchGroup containersGroup = null;

	private OrbitBehavior orbit = null;

	private final Color3f backgroundColor;

	private PickTranslate3DBehavior translation;

	private MouseClickedBehavior selectionBehavior;

	private PickRotate3DBehavior rotate;

	private Background back;

	private boolean initialized = false;

	SceneGraphHandler(SeeIT3DCoreHandler manager) {
		this.manager = manager;
		this.backgroundColor = Preferences.getInstance().getBackgroundColor();
	}

	void clearScene() {
		for (Container container : manager.containersInView()) {
			removeScene(container);
		}
	}

	void enableOrbiting() {
		changeOrbitState(true);
	}

	void disableOrbiting() {
		changeOrbitState(false);
	}

	void removeScene(Container container) {
		ISceneGraphRelationshipGenerator generator = container.getSceneGraphRelationshipGenerator();
		if (generator instanceof PickingCallback) {
			translation.unregisterCallback((PickingCallback) generator);
		}
		BranchGroup containerBG = container.getContainerBG();
		containerBG.detach();
		containersGroup.removeChild(containerBG);
	}

	void setupTranslationCallback(PickingCallback callback) {
		if (translation != null) {
			translation.setupCallback(callback);
		}
	}

	public void unregisterPickingCallback(PickingCallback callback) {
		if (translation != null) {
			translation.unregisterCallback(callback);
		}
	}

	SeeIT3DCanvas getCanvas() {
		checkIfInitialize();
		return canvas;
	}

	void rebuildSceneGraph() {
		checkIfInitialize();
		if (containersGroup != null) {
			containersGroup.detach();
			rootObj.removeChild(containersGroup);
			buildGraph();
		}
	}

	void changeOrbitState(boolean enabled) {
		if (orbit != null) {
			orbit.setRotateEnable(enabled);
			orbit.setTranslateEnable(enabled);
		}
	}

	private void checkIfInitialize() {
		if (!initialized) {
			buildAllVisualization();
		}
	}

	private void buildAllVisualization() {

		if (initialized) {
			throw new SeeIT3DException("The visualization has already been initialized");
		}

		buildUniverseCanvasAndRootObj();
		buildEnvironment();
		addBehaviors();
		buildGraph();

		Utils.buildAxis(rootObj);

		rootObj.compile();
		universe.addBranchGraph(rootObj);

		setViewersPosition(10);

		initialized = true;
	}

	private void buildUniverseCanvasAndRootObj() {

		GraphicsConfiguration configuration = SimpleUniverse.getPreferredConfiguration();

		canvas = new SeeIT3DCanvas(configuration);

		universe = new SimpleUniverse(canvas);

		canvas.getView().setFrontClipDistance(1f);
		canvas.getView().setBackClipDistance(1000.0f);
		canvas.getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

		rootObj = new BranchGroup();
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	}

	private void buildEnvironment() {

		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 2000.0f);
		rootObj.setBounds(bounds);

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

		back = new Background(backgroundColor);
		back.setApplicationBounds(bounds);
		rootObj.addChild(back);

	}

	private void addBehaviors() {

		ViewingPlatform viewingPlatform = universe.getViewingPlatform();

		rotate = new PickRotate3DBehavior(canvas, rootObj, bounds, viewingPlatform);
		rotate.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(rotate);

		selectionBehavior = new MouseClickedBehavior(canvas, rootObj, bounds);
		selectionBehavior.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(selectionBehavior);

		translation = new PickTranslate3DBehavior(canvas, rootObj, bounds, viewingPlatform);
		translation.setTolerance(ViewConstants.PICKING_TOLERANCE);
		rootObj.addChild(translation);

		orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ROTATE | OrbitBehavior.REVERSE_TRANSLATE | OrbitBehavior.STOP_ZOOM);
		// orbit.setRotFactors(0, 0);
		orbit.setMinRadius(10);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	private void buildGraph() throws SeeIT3DException {

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

		List<Container> containersToAdd = new ArrayList<Container>();
		List<Container> containersToDelete = new ArrayList<Container>();

		for (Container container : manager.containersInView()) {
			containersToAdd.addAll(container.getRelatedContainersToShow());
			containersToDelete.addAll(container.getRelatedContainersToHide());
		}

		manager.updateContainersInView(containersToAdd, containersToDelete);

		for (Container container : manager.containersInView()) {
			container.updateVisualRepresentation();
		}

		for (Container container : manager.containersInView()) {
			container.generateSceneGraphRelations();
			BranchGroup containerBG = container.getContainerBG();
			containersTG.addChild(containerBG);
		}

		rootObj.addChild(containersGroup);

	}

	void setViewersPosition(float max) {
		float xMax = Math.max(max, 5);
		ViewingPlatform vp = universe.getViewingPlatform();
		TransformGroup tg = vp.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		tg.getTransform(t3d);
		t3d.lookAt(new Point3d(xMax * 2, xMax, xMax * 2), new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
		// t3d.lookAt(new Point3d(0, 0, 30), new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
		// t3d.lookAt(new Point3d(0, 30, 0), new Point3d(0, 0, 0), new Vector3d(0, 0, -1));
		// t3d.lookAt(new Point3d(30, 0, 0), new Point3d(0, 0, 0), new Vector3d(0, 1, 0));
		t3d.invert();
		tg.setTransform(t3d);
	}

}
