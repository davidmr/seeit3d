/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.view.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import seeit3d.metrics.BaseMetricCalculator;

/**
 * This class allows to transfer metrics instances from the metrics group to the visual property group. Based on the implementation at
 * http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.gef/plugins/org.eclipse.gef/src/org/eclipse/gef/dnd/SimpleObjectTransfer.java?root=Tools_Project&view=co
 * 
 * @author David Montaño
 * 
 */
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