package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.base.model.PolyCylinder;

public final class ChangeTransparencyFunction extends FunctionToApplyOnPolycylinders {

	private final boolean moreTransparent;

	public ChangeTransparencyFunction(boolean moreTransparent) {
		this.moreTransparent = moreTransparent;
	}

	@Override
	public PolyCylinder apply(PolyCylinder poly) {
		poly.changeTransparency(moreTransparent);
		return poly;
	}

}
