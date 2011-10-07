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
package seeit3d.internal.base.core.handler.utils;

import java.util.Iterator;
import java.util.List;

import seeit3d.internal.base.model.PolyCylinder;

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
