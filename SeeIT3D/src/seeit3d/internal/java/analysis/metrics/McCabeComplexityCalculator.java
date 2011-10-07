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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.analysis.metric.AbstractNumericMetricCalculator;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.java.util.LineOfCode;

import com.google.inject.Singleton;

/**
 * Complexity calculator. It is based on the McCabe complexity metric.
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class McCabeComplexityCalculator extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = -8774645887099929391L;

	public static final McCabeComplexityCalculator INSTANCE = new McCabeComplexityCalculator();

	private static final float maxValue = 10.0f;

	public static final String name = "McCabe Complexity";

	private McCabeComplexityCalculator() {
		super(name);
	}

	@Override
	public String calculate(Object element) {
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

}
