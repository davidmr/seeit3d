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
package seeit3d.core.handler.utils;

import java.util.Iterator;
import java.util.List;

import seeit3d.general.model.Container;

/**
 * Iterator on selected containers
 * 
 * @author David Montaño
 * 
 */
public class ContainersSelectedIterator implements Iterator<Container> {

	private final List<Container> containers;

	private int index = 0;

	public ContainersSelectedIterator(List<Container> containers) {
		this.containers = containers;
	}

	@Override
	public boolean hasNext() {
		for (int i = index; i < containers.size(); i++) {
			if (containers.get(i).isSelected()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Container next() {
		for (int i = index; i < containers.size(); i++) {
			if (containers.get(i).isSelected()) {
				index = i + 1;
				return containers.get(i);
			}
		}
		return null;
	}

	@Override
	public void remove() {
		containers.remove(index - 1);
		index--;
	}

}