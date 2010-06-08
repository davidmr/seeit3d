package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.utils.FunctionToApplyOnContainer;

public class PerformOperationOnSelectedContainersEvent implements IEvent {

	private final FunctionToApplyOnContainer function;

	private final boolean visualizationNeedsRefresh;

	public PerformOperationOnSelectedContainersEvent(FunctionToApplyOnContainer function, boolean visualizationNeedsRefresh) {
		this.function = function;
		this.visualizationNeedsRefresh = visualizationNeedsRefresh;
	}

	public FunctionToApplyOnContainer getFunction() {
		return function;
	}

	public boolean isVisualizationNeedsRefresh() {
		return visualizationNeedsRefresh;
	}

}
