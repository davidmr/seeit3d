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
package seeit3d.modelers.java.generator.metrics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;
import seeit3d.general.model.generator.metrics.AbstractContinuousMetricCalculator;
import seeit3d.modelers.java.LineOfCode;

/**
 * Complexity calculator. It is based on the McCabe complexity metric.
 * 
 * @author David Montaño
 * 
 */
@SeeIT3DFactoryEnabled(singleton = true)
public class ComplexityCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = -8774645887099929391L;

	private static final float maxValue = 11.0f;

	public static final String name = "McCabe Complexity";

	public ComplexityCalculator() {
		super(name);
	}

	@Override
	public String calculateMetricValue(Object element) {
		try {
			List<IMethod> methods = new ArrayList<IMethod>();
			if (element instanceof IPackageFragment) {
				IPackageFragment pack = (IPackageFragment) element;
				ICompilationUnit[] compilationUnits = pack.getCompilationUnits();
				for (ICompilationUnit compilationUnit : compilationUnits) {
					IType type = compilationUnit.findPrimaryType();
					if (type != null) {
						methods.addAll(Arrays.asList(type.getMethods()));
					}
				}
			} else if (element instanceof ICompilationUnit) {
				ICompilationUnit compilationUnit = (ICompilationUnit) element;
				IType type = compilationUnit.findPrimaryType();
				if (type != null) {
					methods.addAll(Arrays.asList(type.getMethods()));
				}
			} else if (element instanceof IMethod) {
				IMethod method = (IMethod) element;
				methods.add(method);
			} else if (element instanceof LineOfCode) {
				LineOfCode line = (LineOfCode) element;
				return Integer.toString(calculateComplexity(line.getLine()));
			}
			double complexity = 0;
			for (IMethod method : methods) {
				complexity += calculateComplexity(method);
			}
			if (methods.size() > 0) {
				complexity /= methods.size();
			}

			return new BigDecimal(complexity).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return DEFAULT_VALUE;

	}

	private int calculateComplexity(IMethod method) throws JavaModelException {
		String[] lines = method.getSource().split("\n");
		lines = cleanCode(lines);
		int complexity = 1;
		for (String line : lines) {
			complexity += calculateComplexity(line);
		}
		return complexity;
	}

	private String[] cleanCode(String[] lines) {
		String[] newLines = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			newLines[i] = lines[i].replaceAll(" ", "");
		}
		return newLines;
	}

	private int calculateComplexity(String line) {
		int complexity = 0;
		complexity += countOcurrences(line, "if(");
		complexity += countOcurrences(line, "switch(");
		complexity += countOcurrences(line, "elseif(");
		complexity += countOcurrences(line, "case:");
		complexity += countOcurrences(line, "default:");
		complexity += countOcurrences(line, "for(");
		complexity += countOcurrences(line, "while(");
		return complexity;
	}

	private int countOcurrences(String text, String pattern) {
		int count = 0;
		int index;
		while ((index = text.indexOf(pattern)) != -1) {
			count++;
			text = text.substring(index + pattern.length());
		}
		return count;
	}

	@Override
	public float getMaxValue() {
		return maxValue;
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

}
