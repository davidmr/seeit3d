package seeit3d.visual.api;

import seeit3d.visual.colorscale.IColorScale;
import seeit3d.visual.colorscale.imp.ColdToHotColorScale;

public class DefaultSeeIT3DVisualProperties implements SeeIT3DVisualProperties {

	private IColorScale colorScale;

	public DefaultSeeIT3DVisualProperties() {
		colorScale = new ColdToHotColorScale();
	}

	@Override
	public IColorScale getColorScale() {
		return colorScale;
	}

	@Override
	public void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

}
