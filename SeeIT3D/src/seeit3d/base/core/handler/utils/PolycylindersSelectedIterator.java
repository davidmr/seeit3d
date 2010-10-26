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
package seeit3d.base.core.handler.utils;

import java.util.Iterator;
import java.util.List;

import seeit3d.base.model.PolyCylinder;

/**
 * Iterator over selected polycylinders
 * 
 * @author David Montaño
 * 
 */
public class PolycylindersSelectedIterator implements Iterator<PolyCylinder> {

	private final List<PolyCylinder> polycylinders;

	private int index = 0;

	public PolycylindersSelectedIterator(List<PolyCylinder> containers) {
		this.polycylinders = containers;
	}

	@Override
	public boolean hasNext() {
		for (int i = index; i < polycylinders.size(); i++) {
			if (polycylinders.get(i).isSelected()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public PolyCylinder next() {
		for (int i = index; i < polycylinders.size(); i++) {
			if (polycylinders.get(i).isSelected()) {
				index = i + 1;
				return polycylinders.get(i);
			}
		}
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
