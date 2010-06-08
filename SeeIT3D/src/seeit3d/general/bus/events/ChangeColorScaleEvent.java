package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.visual.colorscale.IColorScale;

public class ChangeColorScaleEvent implements IEvent {

	private final IColorScale colorScale;

	public ChangeColorScaleEvent(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	public IColorScale getColorScale() {
		return colorScale;
	}

}
