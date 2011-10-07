/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.ui.actions;

import seeit3d.internal.base.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.internal.base.model.PolyCylinder;

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
