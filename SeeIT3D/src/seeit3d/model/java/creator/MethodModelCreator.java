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
package seeit3d.model.java.creator;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.LineOfCode;
import seeit3d.model.java.metrics.ControlStructureCalculator;
import seeit3d.model.java.metrics.LOCCalculator;
import seeit3d.utils.Utils;

import com.google.common.collect.Lists;

/**
 * Method analyzer @see AbstracModelCreator
 * 
 * @author David Montaño
 * 
 */
public class MethodModelCreator extends AbstracModelCreator<IMethod, LineOfCode> {

	public MethodModelCreator(IMethod method) {
		super(method);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ControlStructureCalculator.name);
	}

	@Override
	protected LineOfCode[] fetchChildren(IMethod element) throws JavaModelException {
		return Utils.buildLinesFromMethod(element);
	}

	@Override
	protected IModelCreator lowerLevelModelCreator(LineOfCode childrenElement) {
		return null;
	}

}
