package seeit3d.metrics;

import java.util.List;

public abstract class AbstractCategorizedMetricCalculator extends BaseMetricCalculator {

	private static final long serialVersionUID = 3242755411010446121L;

	public AbstractCategorizedMetricCalculator(String name, boolean register) {
		super(name, register);
	}

	public abstract int getNumCategories();

	public abstract List<String> getCategories();

}
