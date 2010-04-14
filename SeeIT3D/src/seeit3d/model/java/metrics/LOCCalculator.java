package seeit3d.model.java.metrics;

import java.util.*;

import org.eclipse.jdt.core.*;

import seeit3d.error.ErrorHandler;
import seeit3d.metrics.AbstractContinuousMetricCalculator;

public class LOCCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = -6148807468764636601L;

	public static final String name = "LOC";

	private static final float maxValue = 400;

	public LOCCalculator() {
		super(name, false);
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
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

	@Override
	public boolean isReplaceable() {
		return false;
	}

}