package seeit3d.base;

import seeit3d.base.core.api.*;
import seeit3d.base.core.handler.*;

import com.google.inject.AbstractModule;

public class SeeIT3DModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IVisualizationStateChecker.class).to(DefaultVisualizationStateChecker.class);
		bind(IVisualizationState.class).to(DefaultVisualizationState.class);
		bind(ISceneGraphManipulator.class).to(DefaultSceneGraphManipulator.class);
		bind(ISeeIT3DCore.class).to(DefaultSeeIT3DCore.class);
	}

}
