package seeit3d.metrics;

public abstract class AbstractContinuousMetricCalculator extends BaseMetricCalculator {

	private static final long serialVersionUID = 5441554592035195840L;

	protected static final String DEFAULT_VALUE = "0.0";

	public AbstractContinuousMetricCalculator(String name, boolean register) {
		super(name, register);
	}

	public abstract float getMaxValue();

}
