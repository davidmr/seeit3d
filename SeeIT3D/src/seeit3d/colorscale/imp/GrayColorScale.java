package seeit3d.colorscale.imp;

import javax.vecmath.Color3f;

import seeit3d.colorscale.IColorScale;

public class GrayColorScale implements IColorScale {

	@Override
	public Color3f generateCuantitavieColor(float value) {
		return new Color3f(value, value, value);
	}

	@Override
	public String getName() {
		return "Gray Scale";
	}

}
