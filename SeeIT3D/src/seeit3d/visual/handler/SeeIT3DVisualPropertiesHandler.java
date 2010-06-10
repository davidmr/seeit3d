package seeit3d.visual.handler;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.ChangeColorScaleEvent;
import seeit3d.general.model.Preferences;
import seeit3d.visual.api.SeeIT3DVisualProperties;
import seeit3d.visual.colorscale.IColorScale;

public class SeeIT3DVisualPropertiesHandler implements SeeIT3DVisualProperties, IEventListener {

	private IColorScale colorScale;

	public SeeIT3DVisualPropertiesHandler() {
		colorScale = Preferences.getInstance().getColorScale();
		registerListener(ChangeColorScaleEvent.class, this);
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof ChangeColorScaleEvent) {
			setColorScale(((ChangeColorScaleEvent) event).getColorScale());
		}

	}

	private void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	@Override
	public IColorScale getColorScale() {
		return colorScale;
	}

}
