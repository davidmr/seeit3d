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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;

import seeit3d.analysis.Child;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.model.IContainerRepresentedObject;
import seeit3d.internal.java.JavaContribution;
import seeit3d.internal.java.analysis.metrics.JavaRepresentation;
import seeit3d.internal.java.analysis.metrics.LOCCalculator;
import seeit3d.internal.java.analysis.metrics.McCabeComplexityCalculator;
import seeit3d.internal.java.util.NameGenerator;

/**
 * Model data provider for types (classes)
 * 
 * @author David Montaño
 * 
 */
public class TypeDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof IType || element instanceof ICompilationUnit;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		IJavaElement type = (IJavaElement) element;
		return new JavaRepresentation(type);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		metrics.add(LOCCalculator.INSTANCE);
		metrics.add(McCabeComplexityCalculator.INSTANCE);
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		try {
			IType type = getType(element);
			if (type != null) {
				IMethod[] methods = type.getMethods();
				List<Child> children = new ArrayList<Child>();
				for (IMethod method : methods) {
					String name = NameGenerator.generateNameFor(method);
					Child child = new Child(name, method, new EclipseJavaRepresentation(type));
					children.add(child);
				}
				return children;
			}
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return JavaContribution.MODEL_PROVIDER_KEY_METHOD;
	}

	@Override
	public List<Object> related(Object element) {
		try {
			IType type = getType(element);
			if (type != null) {
				List<String> relatedTypesNames = new ArrayList<String>();
				List<String> resolvedTokens = new ArrayList<String>();

				IJavaProject project = type.getJavaProject();

				String source = type.getSource();

				IScanner scanner = ToolFactory.createScanner(false, false, false, false);
				scanner.setSource(source.toCharArray());

				while (true) {
					int token = scanner.getNextToken();
					if (token == ITerminalSymbols.TokenNameEOF) {
						break;
					}
					if (token == ITerminalSymbols.TokenNameIdentifier) {
						char[] currentTokenSource = scanner.getCurrentTokenSource();
						String currentToken = new String(currentTokenSource);
						if (!resolvedTokens.contains(currentToken)) {
							String[][] resolveType = type.resolveType(currentToken);
							if (resolveType != null) {
								String elementName = resolveType[0][0] + "." + resolveType[0][1];
								relatedTypesNames.add(elementName);
							}
							resolvedTokens.add(currentToken);
						}
					}
				}

				List<Object> related = new ArrayList<Object>();
				for (String relatedTypeName : relatedTypesNames) {
					IType relatedType = project.findType(relatedTypeName);
					if (relatedType != null && !relatedType.isBinary()) {
						related.add(relatedType);
					}
				}
				return related;
			}
		} catch (Exception e) {
			ErrorHandler.error(e);
		}
		return Collections.emptyList();
	}

	private IType getType(Object element) {
		if (element instanceof IType) {
			return (IType) element;
		} else if (element instanceof ICompilationUnit) {
			return ((ICompilationUnit) element).findPrimaryType();
		}
		return null;

	}

}
