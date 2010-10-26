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
package seeit3d.base;

import seeit3d.base.core.api.*;
import seeit3d.base.core.handler.*;
import seeit3d.base.ui.api.ISeeIT3DUI;
import seeit3d.base.ui.handler.DefaultSeeIT3DUI;
import seeit3d.base.visual.api.*;
import seeit3d.base.visual.colorscale.DefaultColorScaleRegistry;
import seeit3d.base.visual.handler.DefaultSeeIT3DVisualProperties;
import seeit3d.base.visual.relationships.DefaultRelationshipsRegistry;

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
