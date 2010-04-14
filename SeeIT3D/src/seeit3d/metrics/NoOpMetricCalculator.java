package seeit3d.metrics;

import org.eclipse.jdt.core.IJavaElement;

public class NoOpMetricCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = 5995124210204320439L;

	public static final String NAME = "NoOp Metric";

	public NoOpMetricCalculator() {
		super(NAME, false);
	}

	@Override
	public float getMaxValue() {
		return 1;
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
		return "1";
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

}
