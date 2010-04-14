package seeit3d.colorscale.imp;

import javax.vecmath.Color3f;

import seeit3d.colorscale.IColorScale;

public class ColdToHotColorScale implements IColorScale {

	@Override
	public Color3f generateCuantitavieColor(float value) {
		return new Color3f(value, 0.0f, 1.0f - value);
	}
	
	@Override
	public String getName() {
		return "Cold to Hot";
	}

}
