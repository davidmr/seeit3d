package seeit3d.visual.relationships.imp;

import static seeit3d.general.bus.EventBus.*;

import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.general.SeeIT3DAPILocator;
import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.ContainersLayoutDoneEvent;
import seeit3d.general.model.Container;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public abstract class GeometryBasedGenerator implements ISceneGraphRelationshipGenerator, PickingCallback, IEventListener {

	private static final Transform3D EMPTY_TRANSFORMATION = new Transform3D();

	private final Map<Container, Shape3D> relatedShapes;

	private final Map<Container, TransformGroup> relatedTG;

	private final List<Container> relatedContainers;

	private Container relationshipSourceContainer;

	public GeometryBasedGenerator() {
		registerListener(ContainersLayoutDoneEvent.class, this);
		relatedShapes = new TreeMap<Container, Shape3D>();
		relatedTG = new TreeMap<Container, TransformGroup>();
		relatedContainers = new ArrayList<Container>();
	}

	public abstract Geometry createGeometryBetweenContainers(Container origin, Container destination);

	public Transform3D createTransformation(Container origin, Container destination) {
		return EMPTY_TRANSFORMATION;
	}

	@Override
	public final void transformChanged(int type, TransformGroup tg) {
		Container movedContainer = (Container) tg.getParent().getUserData();

		if (movedContainer.equals(relationshipSourceContainer)) {
			for (Container container : relatedContainers) {
				updateConnectionBetweenContainers(container);
			}
			// updateConnectionBetweenContainers(relatedContainers.get(0));
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
	public final List<Container> generateVisualRelationShips(Container baseContainer) {
		List<Container> relatedContainers = baseContainer.getRelatedContainersToShow();

		for (Container related : relatedContainers) {
			initializeRelationBetweenContainers(baseContainer, related);
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

	private void initializeRelationBetweenContainers(Container baseContainer, Container relatedContainer) {
		Geometry geometry = createGeometryBetweenContainers(baseContainer, relatedContainer);

		Appearance appearance = new Appearance();
		LineAttributes lineAttributes = new LineAttributes(2.0f, LineAttributes.PATTERN_SOLID, true);
		appearance.setLineAttributes(lineAttributes);

		ColoringAttributes coloringAttributes = new ColoringAttributes(SeeIT3DAPILocator.findPreferences().getRelationMarkColor(), ColoringAttributes.NICEST);
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
