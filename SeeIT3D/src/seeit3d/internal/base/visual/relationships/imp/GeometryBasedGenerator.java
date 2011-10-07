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

import static seeit3d.internal.base.bus.EventBus.publishEvent;
import static seeit3d.internal.base.bus.EventBus.registerListener;
import static seeit3d.internal.base.bus.EventBus.unregisterListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.ContainersLayoutDoneEvent;
import seeit3d.internal.base.bus.events.RegisterPickingCallbackEvent;
import seeit3d.internal.base.bus.events.UnregisterPickingCallbackEvent;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

/**
 * Template for implementations based on Java3D geometry. It provides a mechanism to update the geometry when the containers are moved
 * 
 * @author David Montaño
 * 
 */
public abstract class GeometryBasedGenerator implements ISceneGraphRelationshipGenerator, PickingCallback, IEventListener {

	private static final Transform3D EMPTY_TRANSFORMATION = new Transform3D();

	private Map<Container, Shape3D> relatedShapes;

	private Map<Container, TransformGroup> relatedTG;

	private List<Container> relatedContainers;

	private Container relationshipSourceContainer;

	@Override
	public void initialize() {
		registerListener(ContainersLayoutDoneEvent.class, this);
		publishEvent(new RegisterPickingCallbackEvent(this));
		relatedShapes = new TreeMap<Container, Shape3D>();
		relatedTG = new TreeMap<Container, TransformGroup>();
		relatedContainers = new ArrayList<Container>();
		SeeIT3D.injector().injectMembers(this);
	}

	public abstract Geometry createGeometryBetweenContainers(Container origin, Container destination);

	public Transform3D createTransformation(Container origin, Container destination) {
		return EMPTY_TRANSFORMATION;
	}

	@Override
	public final void transformChanged(int type, TransformGroup tg) {
		Container movedContainer = (Container) tg.getUserData();

		if (movedContainer.equals(relationshipSourceContainer)) {
			for (Container container : relatedContainers) {
				updateConnectionBetweenContainers(container);
			}
		} else {
			updateConnectionBetweenContainers(movedContainer);
		}
	}

	@Override
	public final void processEvent(IEvent event) {
		if (event instanceof ContainersLayoutDoneEvent) {
			for (Container container : relatedContainers) {
				updateConnectionBetweenContainers(container);
			}
		}
	}

	@Override
	public void unused() {
		unregisterListener(ContainersLayoutDoneEvent.class, this);
		publishEvent(new UnregisterPickingCallbackEvent(this));
	}

	@Override
	public final List<Container> generateVisualRelationShips(Container baseContainer, Color3f relationshipColor) {
		List<Container> relatedContainers = baseContainer.getRelatedContainersToShow();

		for (Container related : relatedContainers) {
			initializeRelationBetweenContainers(baseContainer, related, relationshipColor);
		}

		this.relationshipSourceContainer = baseContainer;
		this.relatedContainers.addAll(relatedContainers);

		return relatedContainers;
	}

	public LineArray createVectorForContainer(Container container) {
		LineArray vector = new LineArray(2, LineArray.COORDINATES);
		vector.setCoordinate(0, new Point3f());
		vector.setCoordinate(1, new Point3f(container.getPosition()));
		return vector;
	}

	private void updateConnectionBetweenContainers(Container destination) {
		if (!relationshipSourceContainer.equals(destination)) {
			Geometry geometry = createGeometryBetweenContainers(relationshipSourceContainer, destination);
			Shape3D relationMark = relatedShapes.get(destination);
			if (relationMark != null) {
				relationMark.setGeometry(geometry);
				Transform3D transformation = createTransformation(relationshipSourceContainer, destination);
				TransformGroup transformGroup = relatedTG.get(destination);
				transformGroup.setTransform(transformation);
			}
		}
	}

	private void initializeRelationBetweenContainers(Container baseContainer, Container relatedContainer, Color3f relationshipColor) {
		Geometry geometry = createGeometryBetweenContainers(baseContainer, relatedContainer);

		Appearance appearance = new Appearance();
		LineAttributes lineAttributes = new LineAttributes(2.0f, LineAttributes.PATTERN_SOLID, true);
		appearance.setLineAttributes(lineAttributes);

		ColoringAttributes coloringAttributes = new ColoringAttributes(relationshipColor, ColoringAttributes.NICEST);
		appearance.setColoringAttributes(coloringAttributes);

		Shape3D relationMark = new Shape3D(geometry, appearance);
		relationMark.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		relationMark.setCapability(Shape3D.ALLOW_GEOMETRY_READ);

		Transform3D transformation = createTransformation(baseContainer, relatedContainer);
		TransformGroup transformGroup = new TransformGroup();
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroup.setTransform(transformation);

		transformGroup.addChild(relationMark);

		baseContainer.getContainerBG().addChild(transformGroup);

		relatedShapes.put(relatedContainer, relationMark);
		relatedTG.put(relatedContainer, transformGroup);
	}

	protected Point3f extractPointFromVector(Vector3f vector) {
		float[] coordinates = new float[3];
		vector.get(coordinates);
		return new Point3f(coordinates);
	}

}
