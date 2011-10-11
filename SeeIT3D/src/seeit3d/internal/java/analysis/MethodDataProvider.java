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
package seeit3d.internal.java.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.analysis.Child;
import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.java.analysis.metrics.ControlStructureCalculator;
import seeit3d.internal.java.analysis.metrics.JavaRepresentation;
import seeit3d.internal.java.analysis.metrics.LOCCalculator;
import seeit3d.internal.java.util.LineOfCode;
import seeit3d.internal.java.util.NameGenerator;
import seeit3d.internal.utils.Utils;

/**
 * Model data provider for methods in types
 * 
 * @author David Montaño
 * 
 */
public class MethodDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof IMethod;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		IMethod method = (IMethod) element;
		return new JavaRepresentation(method);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		metrics.add(LOCCalculator.INSTANCE);
		metrics.add(ControlStructureCalculator.INSTANCE);
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		try {
			IMethod method = (IMethod) element;
			LineOfCode[] lines = Utils.buildLinesFromMethod(method);
			List<Child> children = new ArrayList<Child>();
			for (LineOfCode line : lines) {
				String name = NameGenerator.generateNameFor(line);
				Child child = new Child(name, line, new EclipseJavaRepresentation(method));
				children.add(child);
			}
			return children;
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return null;
	}

	@Override
	public List<Object> related(Object element) {
		return Collections.emptyList();
	}

}
