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
package seeit3d.internal.base.core.handler;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import seeit3d.internal.base.World;
import seeit3d.internal.base.core.api.ISceneGraphManipulator;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.core.api.IVisualizationState;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.ui.behavior.MouseClickedBehavior;
import seeit3d.internal.base.ui.behavior.PickRotate3DBehavior;
import seeit3d.internal.base.ui.behavior.PickTranslate3DBehavior;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;
import seeit3d.internal.utils.Utils;
import seeit3d.internal.utils.ViewConstants;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
@Singleton
public class DefaultSceneGraphManipulator implements ISceneGraphManipulator {

	private final IVisualizationState state;

	private SeeIT3DCanvas canvas = null;

	private BranchGroup rootObj = null;

	private SimpleUniverse universe = null;

	private BranchGroup containersGroup = null;

	private OrbitBehavior orbit = null;

	private final Color3f backgroundColor;

	private PickTranslate3DBehavior translation;

	private MouseClickedBehavior selectionBehavior;

	private PickRotate3DBehavior rotate;

	private Background back;

	private boolean initialized = false;

	@Inject
	public DefaultSceneGraphManipulator(IVisualizationState state, ISeeIT3DPreferences preferences) {
		this.state = state;
		this.backgroundColor = preferences.getBackgroundColor();
	}

	@Override
	public void initialize() {
		buildAllVisualization();
	}

	@Override
	public void clearScene() {
		for (Container container : state.containersInView()) {
			removeScene(container);
		}
	}

	@Override
	public void removeScene(Container container) {
		ISceneGraphRelationshipGenerator generator = container.getSceneGraphRelationshipGenerator();
		if (generator instanceof PickingCallback) {
			translation.unregisterCallback((PickingCallback) generator);
		}
		BranchGroup containerBG = container.getContainerBG();
		containerBG.detach();
		containersGroup.removeChild(containerBG);
	}

	@Override
	public void setupTranslationCallback(PickingCallback callback) {
		if (translation != null) {
			translation.setupCallback(callback);
		}
	}

	@Override
	public void unregisterPickingCallback(PickingCallback callback) {
		if (translation != null) {
			translation.unregisterCallback(callback);
		}
	}

	@Override
	public void rebuildSceneGraph() {
		checkIfInitialize();
		if (containersGroup != null) {
			containersGroup.detach();
			rootObj.removeChild(containersGroup);
			buildGraph();
		}
	}

	@Override
	public void changeOrbitState(boolean enabled) {
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

		rootObj = new BranchGroup();
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		rootObj.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		canvas = new SeeIT3DCanvas(configuration, rootObj);

		universe = new SimpleUniverse(canvas);

		canvas.getView().setFrontClipDistance(1f);
		canvas.getView().setBackClipDistance(1000.0f);
		canvas.getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

	}

	private void buildEnvironment() {

		Bounds bounds = World.bounds;
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

		Bounds bounds = World.bounds;
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

		for (Container container : state.containersInView()) {
			containersToAdd.addAll(container.getRelatedContainersToShow());
			containersToDelete.addAll(container.getRelatedContainersToHide());
		}

		state.updateContainersInView(containersToAdd, containersToDelete);

		try {
			for (Container container : state.containersInView()) {
				container.updateVisualRepresentation();
			}

			for (Container container : state.containersInView()) {
				container.generateSceneGraphRelations();
				BranchGroup containerBG = container.getContainerBG();
				containersTG.addChild(containerBG);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		rootObj.addChild(containersGroup);

	}

	@Override
	public void setViewersPosition(float max) {
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

	@Override
	public void activateSelectionTool() {
		canvas.activateSelectionTool();
	}

	@Override
	public SeeIT3DCanvas getCanvas() {
		checkIfInitialize();
		return canvas;
	}

}
