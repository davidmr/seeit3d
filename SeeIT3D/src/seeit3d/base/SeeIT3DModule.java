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
