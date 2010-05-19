package seeit3d.relationships.imp;

import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.Vector3f;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.model.representation.Container;
import seeit3d.relationships.RelationShipVisualGenerator;
import seeit3d.utils.Utils;

import com.sun.j3d.utils.geometry.Box;

public class CommonBaseGenerator implements RelationShipVisualGenerator {

	public static final float RELATION_MARK_PADDING = 0.1f;

	public static final float RELATION_MARK_SCALING = 1.2f;

	private static final String NAME = "Common Base";

	private final SeeIT3DManager manager;

	public CommonBaseGenerator() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {

		addMarkToContainer(baseContainer);
		for (Container related : baseContainer.getRelatedContainers()) {
			addMarkToContainer(related);
		}

		return baseContainer.getRelatedContainers();

	}

	private void addMarkToContainer(Container container) {

		TransformGroup transformGroup = container.getTransformGroup();
		float width = container.getWidth();
		float height = container.getHeight();
		float depth = container.getDepth();

		Box surroundingBox = new Box(width * RELATION_MARK_SCALING, height, depth * RELATION_MARK_SCALING, null);

		Appearance app = new Appearance();
		ColoringAttributes color = new ColoringAttributes(manager.getRelationMarkColor(), ColoringAttributes.SHADE_FLAT);
		app.setColoringAttributes(color);

		TransformGroup tgRelationMarkTop = new TransformGroup();
		Geometry geometryTop = surroundingBox.getShape(Box.TOP).getGeometry();
		Shape3D relationMarkTop = new Shape3D(geometryTop, app);
		tgRelationMarkTop.addChild(relationMarkTop);

		Utils.translateTranformGroup(tgRelationMarkTop, new Vector3f(0.0f, -(2 * height + RELATION_MARK_PADDING), 0.0f));

		TransformGroup tgRelationMarkBottom = new TransformGroup();
		Geometry geometryBottom = surroundingBox.getShape(Box.BOTTOM).getGeometry();
		Shape3D relationMarkBottom = new Shape3D(geometryBottom, app);
		tgRelationMarkBottom.addChild(relationMarkBottom);

		Utils.translateTranformGroup(tgRelationMarkBottom, new Vector3f(0.0f, -RELATION_MARK_PADDING, 0.0f));

		BranchGroup mark = new BranchGroup();

		mark.addChild(tgRelationMarkBottom);
		mark.addChild(tgRelationMarkTop);

		transformGroup.addChild(mark);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void transformChanged(int type, TransformGroup tg) {}

}
