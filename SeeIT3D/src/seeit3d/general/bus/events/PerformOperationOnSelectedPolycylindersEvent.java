package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.utils.FunctionToApplyOnPolycylinders;

public class PerformOperationOnSelectedPolycylindersEvent implements IEvent {

	private final FunctionToApplyOnPolycylinders function;

	private final boolean visualizationNeedsRefresh;

	public PerformOperationOnSelectedPolycylindersEvent(FunctionToApplyOnPolycylinders function, boolean visualizationNeedsRefresh) {
		this.function = function;
		this.visualizationNeedsRefresh = visualizationNeedsRefresh;
	}

	public FunctionToApplyOnPolycylinders getFunction() {
		return function;
	}

	public boolean isVisualizationNeedsRefresh() {
		return visualizationNeedsRefresh;
	}
}
