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
package seeit3d.java.modeler.generator.metrics;

import java.util.List;

import seeit3d.base.model.generator.metrics.AbstractNominalMetricCalculator;
import seeit3d.java.modeler.LineOfCode;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

/**
 * Control structure calculator. It looks for control structure text within the source code line.
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class ControlStructureCalculator extends AbstractNominalMetricCalculator {

	private static final long serialVersionUID = 8927710809333793473L;

	private static final String FOR = "for";

	private static final String WHILE = "while";

	private static final String IF = "if";

	private static final String NONE = "none";

	private static final List<String> categories = Lists.newArrayList(FOR, WHILE, IF, NONE);

	public static final String name = "Control Structure";

	public ControlStructureCalculator() {
		super(name);
	}

	@Override
	public String calculateMetricValue(Object element) {
		String source = null;
		if (element instanceof LineOfCode) {
			LineOfCode line = (LineOfCode) element;
			source = line.getLine();
			if (source.contains("for")) {
				return FOR;
			}
			if (source.contains("while")) {
				return WHILE;
			}
			if (source.contains("if")) {
				return IF;
			}
		}
		return NONE;
	}

	@Override
	public int getNumCategories() {
		return categories.size();
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

}
