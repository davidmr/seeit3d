/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.base.model.PolyCylinder;

/**
 * This class changes the polycylinder transparency
 * 
 * @author David Montaño
 * 
 */
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
