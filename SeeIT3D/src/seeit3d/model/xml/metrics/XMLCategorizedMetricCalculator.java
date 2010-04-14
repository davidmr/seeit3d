package seeit3d.model.xml.metrics;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

import seeit3d.metrics.AbstractCategorizedMetricCalculator;

public class XMLCategorizedMetricCalculator extends AbstractCategorizedMetricCalculator {

	private static final long serialVersionUID = -7574154133744003269L;

	private final List<String> categories;

	private final int numCategories;

	public XMLCategorizedMetricCalculator(String name, List<String> categories, int numCategories) {
		super(name, true);
		this.categories = categories;
		this.numCategories = numCategories;
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	@Override
	public int getNumCategories() {
		return numCategories;
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
		// nothing to do already calculated
		return null;
	}

	@Override
	public boolean isReplaceable() {
		return true;
	}

}
