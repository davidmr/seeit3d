package seeit3d.preferences;

import javax.vecmath.Color3f;

import seeit3d.colorscale.IColorScale;

public interface IPreferencesListener {

	public void colorScaleChanged(IColorScale newColorScale);

	public void scaleStepChanged(double newScale);

	public void containersPerRowChanged(int newContainersPerRow);

	public void backgroundColorChanged(Color3f newBackgroundColor);

	public void polycylindersPerRowChanged(int newPolycylinderPerRow);

	public void highlightColorChanged(Color3f newHighlightColor);

	public void relationMarkColorChanged(Color3f newRelationMarkColor);

	public void transparencyStepChanged(float transparencyStepChanged);

}
