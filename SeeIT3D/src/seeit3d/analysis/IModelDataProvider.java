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
package seeit3d.analysis;

import java.util.List;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.model.IContainerRepresentedObject;

/**
 * Interface that must be implemented by all object that want to provide visualization data
 * 
 * @author David Montaño
 * 
 */
public interface IModelDataProvider {

	boolean accepts(Object element);

	IContainerRepresentedObject representedObject(Object element);

	List<MetricCalculator> metrics(Object element);

	List<Child> children(Object element);

	String getChildrenModelGeneratorKey();

	List<Object> related(Object element);

}
