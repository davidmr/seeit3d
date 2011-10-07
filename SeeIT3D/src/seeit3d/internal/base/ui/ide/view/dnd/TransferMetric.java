/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.ui.ide.view.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import seeit3d.analysis.metric.MetricCalculator;

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

	private MetricCalculator metric;

	private long startTime;

	public TransferMetric() {
		MYTYPEID = registerType(MYTYPENAME);
	}

	public MetricCalculator getMetric() {
		return metric;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (object instanceof MetricCalculator) {
			setMetric((MetricCalculator) object);
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

	public void setMetric(MetricCalculator metric) {
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