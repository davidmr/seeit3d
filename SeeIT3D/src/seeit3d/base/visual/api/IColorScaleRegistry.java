package seeit3d.base.visual.api;

import seeit3d.base.visual.colorscale.IColorScale;

public interface IColorScaleRegistry {

	void registerColorScale(IColorScale... colorScale);

	Iterable<IColorScale> allColorScales();

	IColorScale findByName(String colorScaleName);

}
