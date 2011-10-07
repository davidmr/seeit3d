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
package seeit3d.internal.base;

import seeit3d.internal.base.core.api.ISceneGraphManipulator;
import seeit3d.internal.base.core.api.ISeeIT3DCore;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.core.api.IVisualizationState;
import seeit3d.internal.base.core.api.IVisualizationStateChecker;
import seeit3d.internal.base.core.handler.DefaultSceneGraphManipulator;
import seeit3d.internal.base.core.handler.DefaultSeeIT3DCore;
import seeit3d.internal.base.core.handler.DefaultSeeIT3DPreferences;
import seeit3d.internal.base.core.handler.DefaultVisualizationState;
import seeit3d.internal.base.core.handler.DefaultVisualizationStateChecker;
import seeit3d.internal.base.ui.api.ISeeIT3DUI;
import seeit3d.internal.base.ui.handler.DefaultSeeIT3DUI;
import seeit3d.internal.base.visual.api.IColorScaleRegistry;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;
import seeit3d.internal.base.visual.api.ISeeIT3DVisualProperties;
import seeit3d.internal.base.visual.colorscale.DefaultColorScaleRegistry;
import seeit3d.internal.base.visual.handler.DefaultSeeIT3DVisualProperties;
import seeit3d.internal.base.visual.relationships.DefaultRelationshipsRegistry;

import com.google.inject.AbstractModule;

/**
 * Guice module that specifies the dependency injection within SeeIT 3D
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ISeeIT3DPreferences.class).to(DefaultSeeIT3DPreferences.class);

		bind(IVisualizationStateChecker.class).to(DefaultVisualizationStateChecker.class);
		bind(IVisualizationState.class).to(DefaultVisualizationState.class);
		bind(ISceneGraphManipulator.class).to(DefaultSceneGraphManipulator.class);
		bind(ISeeIT3DCore.class).to(DefaultSeeIT3DCore.class);

		bind(ISeeIT3DUI.class).to(DefaultSeeIT3DUI.class).asEagerSingleton();

		bind(ISeeIT3DVisualProperties.class).to(DefaultSeeIT3DVisualProperties.class);
		bind(IColorScaleRegistry.class).to(DefaultColorScaleRegistry.class);
		bind(IRelationshipsRegistry.class).to(DefaultRelationshipsRegistry.class);



	}

}
