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
import seeit3d.utils.ViewConstants;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public abstract class GeometryBasedGenerator implements ISceneGraphRelationshipGenerator, PickingCallback, IEventListener {

	private static final Transform3D EMPTY_TRANSFORMATION = new Transform3D();

	private final Map<Container, Shape3D> relatedShapes;

	private final Map<Container, TransformGroup> relatedTG;

	private final List<Container> relatedContainers;

	private Container relationshipSourceContainer;

	private Shape3D shapePos1;

	private Shape3D shapePos2;

	private Shape3D perpendicularShape;

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
		List<Container> relatedContainers = baseContainer.getRelatedContainers();

		for (Container related : relatedContainers) {
			initializeRelationBetweenContainers(baseContainer, related);
		}

		this.relationshipSourceContainer = baseContainer;
		this.relatedContainers.addAll(relatedContainers);

		BranchGroup utilBG = utilsBG(baseContainer);

		baseContainer.getContainerBG().addChild(utilBG);

		return relatedContainers;
	}

	private BranchGroup utilsBG(Container baseContainer) {

		BranchGroup utilBG = new BranchGroup();

		Appearance app1 = new Appearance();
		app1.setLineAttributes(new LineAttributes(1f, LineAttributes.PATTERN_SOLID, true));
		app1.setColoringAttributes(new ColoringAttributes(ViewConstants.BLACK, ColoringAttributes.ALLOW_COLOR_READ));

		LineArray pos1Vector = createVectorForContainer(baseContainer);

		shapePos1 = new Shape3D(pos1Vector, app1);
		shapePos1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		utilBG.addChild(shapePos1);

		Appearance app2 = new Appearance();
		app2.setLineAttributes(new LineAttributes(1f, LineAttributes.PATTERN_SOLID, true));
		app2.setColoringAttributes(new ColoringAttributes(ViewConstants.WHITE, ColoringAttributes.ALLOW_COLOR_READ));

		LineArray pos2Vector = createVectorForContainer(baseContainer);

		shapePos2 = new Shape3D(pos2Vector, app2);
		shapePos2.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		utilBG.addChild(shapePos2);

		Appearance app3 = new Appearance();
		app3.setLineAttributes(new LineAttributes(1f, LineAttributes.PATTERN_SOLID, true));
		app3.setColoringAttributes(new ColoringAttributes(ViewConstants.YELLOW, ColoringAttributes.ALLOW_COLOR_READ));

		LineArray pos3Vector = createVectorForContainer(baseContainer);

		perpendicularShape = new Shape3D(pos3Vector, app3);
		perpendicularShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		utilBG.addChild(perpendicularShape);
		return utilBG;
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
			
			LineArray vectorPos1 = createVectorForContainer(relationshipSourceContainer);
			shapePos1.setGeometry(vectorPos1);

			LineArray vectorPos2 = createVectorForContainer(destination);
			shapePos2.setGeometry(vectorPos2);
			
			LineArray perpendicular = new LineArray(2, LineArray.COORDINATES);
			Vector3f perVector = new Vector3f();
			perVector.cross(relationshipSourceContainer.getPosition(), destination.getPosition());

			perpendicular.setCoordinate(0, new Point3f());
			perpendicular.setCoordinate(1, new Point3f(perVector));

			perpendicularShape.setGeometry(perpendicular);

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
