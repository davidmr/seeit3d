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
package seeit3d.model;

import seeit3d.model.representation.Container;
import seeit3d.utils.Utils;

/**
 * Implementation of <code>IModelCreator</code> which avoids null handling when no creator is needed.
 * 
 * @author David Montaño
 * 
 */
public class NoOpCreator implements IModelCreator {

	@Override
	public Container analize(boolean includeDependecies) {
		return Utils.emptyContainer();
	}

	@Override
	public void analizeAndRegisterInView(boolean includeDependecies) {

	}

}
