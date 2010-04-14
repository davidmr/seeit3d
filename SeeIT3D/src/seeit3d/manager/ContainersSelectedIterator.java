package seeit3d.manager;

import java.util.Iterator;
import java.util.List;

import seeit3d.model.representation.Container;

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