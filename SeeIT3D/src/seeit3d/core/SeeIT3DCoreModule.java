package seeit3d.core;

import seeit3d.core.api.*;
import seeit3d.core.handler.*;

import com.google.inject.AbstractModule;

public class SeeIT3DCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ISeeIT3DCore.class).to(DefaultSeeIT3DCore.class);
		bind(IVisualizationState.class).to(DefaultVisualizationState.class);
		bind(ISceneGraphManipulator.class).to(DefaultSceneGraphManipulator.class);
		bind(IVisualizationStateChecker.class).to(DefaultVisualizationStateChecker.class);
	}

}
