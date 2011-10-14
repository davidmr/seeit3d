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
package seeit3d.jobs;

import static seeit3d.internal.ModelDataProviderRegistry.getModelGenerator;
import static seeit3d.internal.base.bus.EventBus.publishEvent;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;

import seeit3d.analysis.IModelDataProvider;
import seeit3d.internal.base.analysis.ModelGenerator;
import seeit3d.internal.base.bus.events.AddContainerEvent;
import seeit3d.internal.base.bus.events.OpenSeeIT3DViewEvent;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.base.model.Container;

/**
 * Generic visualization job to show information in the visualization area. It needs a model generator key (defined in a contribution) that will be used to generate all the data necessary in the
 * visualization.
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJob extends Job {

	private final String modelGeneratorKey;

	private final List<?> objects;

	public VisualizeJob(Shell shell, String modelGeneratorKey, List<?> objects) {
		super("Visualize in SeeIT 3D");
		this.modelGeneratorKey = modelGeneratorKey;
		this.objects = objects;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		List<Container> containers = new ArrayList<Container>();
		IModelDataProvider provider = getModelGenerator(modelGeneratorKey);
		ModelGenerator generator = new ModelGenerator(provider);
		try {
			for (Object object : objects) {
				Container container = generator.analize(object, true, monitor);
				if (container != null) {
					containers.add(container);
				}
			}
		} catch (SeeIT3DException e) {
			ErrorHandler.error(e);
		}
		monitor.done();

		publishEvent(new AddContainerEvent(containers));
		publishEvent(new OpenSeeIT3DViewEvent());

		return Status.OK_STATUS;
	}

}
