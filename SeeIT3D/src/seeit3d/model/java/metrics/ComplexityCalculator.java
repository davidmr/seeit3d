package seeit3d.model.java.metrics;

import java.math.BigDecimal;
import java.util.*;

import org.eclipse.jdt.core.*;

import seeit3d.error.ErrorHandler;
import seeit3d.metrics.AbstractContinuousMetricCalculator;
import seeit3d.model.representation.LineOfCode;

public class ComplexityCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = -8774645887099929391L;

	private static final float maxValue = 11.0f;

	public static final String name = "McCabe Complexity";

	public ComplexityCalculator() {
		super(name, false);
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
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
				// TODO find out why this happens on struts2
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
