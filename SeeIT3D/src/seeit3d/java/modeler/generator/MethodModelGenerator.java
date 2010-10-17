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
package seeit3d.java.modeler.generator;

import static com.google.common.collect.Lists.*;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.LineOfCode;
import seeit3d.java.modeler.generator.metrics.annotation.ControlStructure;
import seeit3d.java.modeler.generator.metrics.annotation.LOC;
import seeit3d.utils.Utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Method analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class MethodModelGenerator extends AbstracModelGenerator<IMethod, LineOfCode> {

	private final List<MetricCalculator> metrics;

	@Inject
	public MethodModelGenerator(			
			@LOC MetricCalculator loc,
			@ControlStructure MetricCalculator mccabe) {
		super(null);
		this.metrics = newArrayList(loc, mccabe);
	}

	@Override
	protected List<MetricCalculator> getMetrics() {
		return metrics;

	}

	@Override
	protected LineOfCode[] fetchChildren(IMethod element) throws JavaModelException {
		return Utils.buildLinesFromMethod(element);
	}

}
