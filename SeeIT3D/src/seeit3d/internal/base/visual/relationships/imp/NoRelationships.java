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

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;

import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

/**
 * Null relationship generator. It generates nothing
 * 
 * @author David Montaño
 * 
 */
public class NoRelationships implements ISceneGraphRelationshipGenerator {

	private static final String NAME = "No Visual Relation";

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer, Color3f relationshipColor) {
		return new ArrayList<Container>();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void unused() {

	}

}
