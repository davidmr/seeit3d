/**
 * Copyright (C) 2010  David Monta�o
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
package seeit3d.ui.ide.commands;

import org.eclipse.core.commands.*;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.events.PerformOperationOnSelectedPolycylindersEvent;
import seeit3d.general.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.general.model.PolyCylinder;

/**
 * Base command to change transparency on polycylinders
 * 
 * @author David Monta�o
 * 
 */
public abstract class ChangeTransparencyBaseCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ChangeTransparencyFunction function = new ChangeTransparencyFunction(moreTransparent());
		PerformOperationOnSelectedPolycylindersEvent operation = new PerformOperationOnSelectedPolycylindersEvent(function, false);
		publishEvent(operation);
		return null;
	}

	protected abstract boolean moreTransparent();

	private final class ChangeTransparencyFunction extends FunctionToApplyOnPolycylinders {

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

}