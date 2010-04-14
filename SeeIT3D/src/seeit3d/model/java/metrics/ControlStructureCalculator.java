package seeit3d.model.java.metrics;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

import seeit3d.metrics.AbstractCategorizedMetricCalculator;
import seeit3d.model.representation.LineOfCode;

import com.google.common.collect.Lists;

public class ControlStructureCalculator extends AbstractCategorizedMetricCalculator {

	private static final long serialVersionUID = 8927710809333793473L;

	private static final String FOR = "for";

	private static final String WHILE = "while";

	private static final String IF = "if";

	private static final String NONE = "none";

	private static final List<String> categories = Lists.newArrayList(FOR, WHILE, IF, NONE);

	public static final String name = "Control Structure";

	public ControlStructureCalculator() {
		super(name, false);
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
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

	@Override
	public boolean isReplaceable() {
		return false;
	}

}
