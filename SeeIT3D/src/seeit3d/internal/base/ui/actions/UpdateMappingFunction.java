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
package seeit3d.internal.base.ui.actions;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.VisualProperty;

/**
 * This class is in charge of applying the specified mapping to the containers
 * 
 * @author David Montaño
 * 
 */
public class UpdateMappingFunction extends FunctionToApplyOnContainer {

	private final VisualProperty visualProp;
	private final MetricCalculator metric;

	public UpdateMappingFunction(VisualProperty visualProp, MetricCalculator metric) {
		this.visualProp = visualProp;
		this.metric = metric;
	}

	@Override
	public Container apply(Container container) {
		container.updateMapping(metric, visualProp);
		return container;
	}

}
