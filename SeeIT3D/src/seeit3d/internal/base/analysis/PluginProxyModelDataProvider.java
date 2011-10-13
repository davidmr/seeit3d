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
package seeit3d.internal.base.analysis;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import seeit3d.analysis.Child;
import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.utils.Log;

public class PluginProxyModelDataProvider implements IModelDataProvider {

	private final IConfigurationElement element;

	private IModelDataProvider provider;

	public PluginProxyModelDataProvider(IConfigurationElement element) {
		this.element = element;
	}

	private void initInstance() {
		if (provider == null) {
			try {
				Log.i("Initializing external model provider " + element.getAttribute("id"));
				provider = (IModelDataProvider) element.createExecutableExtension("class");
			} catch (Exception e) {
				throw new SeeIT3DException(e.getMessage());
			}
		}
	}

	@Override
	public boolean accepts(Object element) {
		initInstance();
		return provider.accepts(element);
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		initInstance();
		return provider.representedObject(element);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		initInstance();
		return provider.metrics(element);
	}

	@Override
	public List<Child> children(Object element) {
		initInstance();
		return provider.children(element);
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		initInstance();
		return provider.getChildrenModelGeneratorKey();
	}

	@Override
	public List<Object> related(Object element) {
		initInstance();
		return provider.related(element);
	}

}
