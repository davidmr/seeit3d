package seeit3d.model.xml.metrics;

import org.eclipse.jdt.core.IJavaElement;

import seeit3d.metrics.AbstractContinuousMetricCalculator;

public class XMLContinuousMetricCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = -1527870355609839773L;

	private final float maxValue;

	public XMLContinuousMetricCalculator(String name, float maxValue) {
		super(name, true);
		this.maxValue = maxValue;
	}

	@Override
	public float getMaxValue() {
		return maxValue;
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
