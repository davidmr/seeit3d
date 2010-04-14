package seeit3d.view.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import seeit3d.metrics.BaseMetricCalculator;

public class TransferMetric extends ByteArrayTransfer {

	private static final TransferMetric instance = new TransferMetric();

	public static TransferMetric getInstance() {
		return instance;
	}

	private final String MYTYPENAME = "seeit3d.dnd.metricsTransfer";

	private final int MYTYPEID;

	private BaseMetricCalculator metric;

	private long startTime;

	public TransferMetric() {
		MYTYPEID = registerType(MYTYPENAME);
	}

	public BaseMetricCalculator getMetric() {
		return metric;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (object instanceof BaseMetricCalculator) {
			setMetric((BaseMetricCalculator) object);
			startTime = System.currentTimeMillis();
			if (transferData != null)
				super.javaToNative(String.valueOf(startTime).getBytes(), transferData);
		}
	}

	public Object nativeToJava(TransferData transferData) {
		byte bytes[] = (byte[]) super.nativeToJava(transferData);
		if (bytes == null) {
			return null;
		}
		long startTime = Long.parseLong(new String(bytes));
		return (this.startTime == startTime) ? getMetric() : null;
	}

	public void setMetric(BaseMetricCalculator metric) {
		this.metric = metric;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { MYTYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { MYTYPENAME };
	}

}