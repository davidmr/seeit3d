package seeit3d.visual.relationships.imp;

import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.model.Container;
import seeit3d.utils.Utils;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.geometry.Box;

public class CommonBaseGenerator implements ISceneGraphRelationshipGenerator {

	private static final float COLOR_OFFSET = 0.2f;

	public static final float RELATION_MARK_PADDING = 0.1f;

	public static final float RELATION_MARK_SCALING = 1.2f;

	private static final String NAME = "Common Base";

	private final SeeIT3DManager manager;

	public CommonBaseGenerator() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {

		Color3f baseColor = manager.getRelationMarkColor();
		Color3f darkerColor = new Color3f(baseColor.x - COLOR_OFFSET, baseColor.y - COLOR_OFFSET, baseColor.z - COLOR_OFFSET);

		addMarkToContainer(baseContainer, darkerColor);

		for (Container related : baseContainer.getRelatedContainers()) {
			addMarkToContainer(related, baseColor);
		}

		return baseContainer.getRelatedContainers();

	}

	private void addMarkToContainer(Container container, Color3f color) {

		TransformGroup transformGroup = container.getTransformGroup();
		float width = container.getWidth();
		float height = container.getHeight();
		float depth = container.getDepth();

		Box surroundingBox = new Box(width * RELATION_MARK_SCALING, height, depth * RELATION_MARK_SCALING, null);

		Appearance app = new Appearance();
		ColoringAttributes colorAttr = new ColoringAttributes(color, ColoringAttributes.SHADE_FLAT);
		app.setColoringAttributes(colorAttr);

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

}
