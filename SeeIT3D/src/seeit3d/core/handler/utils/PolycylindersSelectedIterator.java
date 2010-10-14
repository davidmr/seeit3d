package seeit3d.core.handler.utils;

import java.util.Iterator;
import java.util.List;

import seeit3d.general.model.PolyCylinder;

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
