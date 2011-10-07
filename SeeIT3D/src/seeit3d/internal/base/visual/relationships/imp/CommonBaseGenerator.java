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

import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Geometry;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;
import seeit3d.internal.utils.Utils;

import com.sun.j3d.utils.geometry.Box;

/**
 * Relationship generator based on a common based applied to all related elements
 * 
 * @author David Montaño
 * 
 */
public class CommonBaseGenerator implements ISceneGraphRelationshipGenerator {

	private static final float COLOR_OFFSET = 0.2f;

	public static final float RELATION_MARK_PADDING = 0.1f;

	public static final float RELATION_MARK_SCALING = 1.2f;

	private static final String NAME = "Common Base";

	public CommonBaseGenerator() {}

	@Override
	public void initialize() {

	}

	@Override
	public void unused() {

	}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer, Color3f relationshipColor) {

		Color3f baseColor = relationshipColor;
		Color3f darkerColor = new Color3f(baseColor.x - COLOR_OFFSET, baseColor.y - COLOR_OFFSET, baseColor.z - COLOR_OFFSET);

		addMarkToContainer(baseContainer, darkerColor);

		for (Container related : baseContainer.getRelatedContainersToShow()) {
			addMarkToContainer(related, baseColor);
		}

		return baseContainer.getRelatedContainersToShow();

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
