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

import java.util.*;

import org.eclipse.jdt.core.*;

import seeit3d.base.error.ErrorHandler;
import seeit3d.base.model.generator.metrics.AbstractNumericMetricCalculator;

import com.google.inject.Singleton;

/**
 * Lines of code calculator. Only takes into account the lines of actual code, not comments
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class LOCCalculator extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = -6148807468764636601L;

	public static final String name = "LOC";

	private static final float maxValue = 400;

	public LOCCalculator() {
		super(name);
	}

	@Override
	public String calculateMetricValue(Object element) {
		try {
			String source = "";
			if (element instanceof IPackageFragment) {
				IPackageFragment pack = (IPackageFragment) element;
				ICompilationUnit[] compilationUnits = pack.getCompilationUnits();
				for (ICompilationUnit iCompilationUnit : compilationUnits) {
					source += iCompilationUnit.getSource();
				}
			} else if (element instanceof ICompilationUnit) {
				ICompilationUnit cu = (ICompilationUnit) element;
				source = cu.getSource();
			} else if (element instanceof IMethod) {
				IMethod method = (IMethod) element;
				source = method.getSource();
			}
			List<String> lines = cleanLines(source.split("\n"));

			return String.valueOf(lines.size());
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return DEFAULT_VALUE;
	}

	private List<String> cleanLines(String[] lines) {

		List<String> newLines = clearSingleLineComments(lines);
		clearMultiLineComments(newLines);
		return newLines;
	}

	private List<String> clearSingleLineComments(String[] lines) {
		List<String> newLines = new ArrayList<String>();
		for (String line : lines) {
			if (!line.trim().startsWith("//")) {
				newLines.add(line);
			}
		}
		return newLines;
	}

	private void clearMultiLineComments(List<String> newLines) {
		boolean inComment = false;
		for (Iterator<String> iterator = newLines.iterator(); iterator.hasNext();) {
			String line = iterator.next().replace(" ", "");
			if (line.trim().startsWith("/*")) {
				inComment = true;
			}
			if (inComment && line.trim().endsWith("*/")) {
				iterator.remove();
				inComment = false;
			} else if (inComment) {
				iterator.remove();
			}
		}
	}

	@Override
	public float getMaxValue() {
		return maxValue;
	}


}