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
package seeit3d.internal.java.analysis.metrics;

import java.util.List;

import seeit3d.analysis.metric.AbstractNominalMetricCalculator;
import seeit3d.internal.java.util.LineOfCode;

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

	public static final ControlStructureCalculator INSTANCE = new ControlStructureCalculator();

	private static final String FOR = "for";

	private static final String WHILE = "while";

	private static final String IF = "if";

	private static final String NONE = "none";

	private static final List<String> categories = Lists.newArrayList(FOR, WHILE, IF, NONE);

	public static final String name = "Control Structure";

	private ControlStructureCalculator() {
		super(name);
	}

	@Override
	public String calculate(Object element) {
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
	public List<String> getCategories() {
		return categories;
	}

}
