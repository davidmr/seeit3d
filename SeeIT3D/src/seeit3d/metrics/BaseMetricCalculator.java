package seeit3d.metrics;

import java.io.Serializable;

import org.eclipse.jdt.core.IJavaElement;

public abstract class BaseMetricCalculator implements Serializable {

	private static final long serialVersionUID = -5439857159491897341L;

	public static final String CONTINUOUS = "CONTINUOUS";

	public static final String CATEGORIZED = "CATEGORIZED";

	private final String name;

	protected BaseMetricCalculator(String name, boolean register) {
		this.name = name;
		if (register) {
			MetricsRegistry.getInstance().registerMetric(this);
		}
		checkAuthorizedSubclassing();
	}

	private void checkAuthorizedSubclassing() {
		boolean authorizedSubclass = false;
		if (this instanceof AbstractCategorizedMetricCalculator || this instanceof AbstractContinuousMetricCalculator) {
			authorizedSubclass = true;
		}

		if (!authorizedSubclass) {
			throw new IllegalStateException("Direct subclassing " + this.getClass().getName() + " is no authorized. Use " + AbstractCategorizedMetricCalculator.class.getName() + " or "
					+ AbstractContinuousMetricCalculator.class.getName());
		}
	}

	public final String getMetricName() {
		return name;
	}

	public abstract boolean isReplaceable();

	// TODO make IJavaElement dependency, an abstraction
	public abstract String calculateMetricValue(IJavaElement element);

}
