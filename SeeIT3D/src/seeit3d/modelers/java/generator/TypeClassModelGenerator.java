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
package seeit3d.modelers.java.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import seeit3d.general.model.Container;
import seeit3d.general.model.generator.IModelGenerator;
import seeit3d.general.model.generator.metrics.MetricCalculator;
import seeit3d.modelers.java.generator.metrics.ComplexityCalculator;
import seeit3d.modelers.java.generator.metrics.LOCCalculator;

/**
 * Class/Type analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
public class TypeClassModelGenerator extends AbstracModelGenerator<IType, IMethod> {

	public TypeClassModelGenerator(IType elementToAnalize) {
		super(elementToAnalize);
	}

	@Override
	protected List<Class<? extends MetricCalculator>> getMetricNames() {
		List<Class<? extends MetricCalculator>> classes = new ArrayList<Class<? extends MetricCalculator>>();
		classes.add(LOCCalculator.class);
		classes.add(ComplexityCalculator.class);
		return classes;
	}

	@Override
	protected IMethod[] fetchChildren(IType element) throws JavaModelException {
		return element.getMethods();
	}

	@Override
	protected IModelGenerator lowerLevelModelGenerator(IMethod childrenElement) {
		return new MethodModelGenerator(childrenElement);
	}

	@Override
	protected void extraOperationsOnContainer(Container createdContainer, IType element) throws JavaModelException {
		if (analizeDependecies) {
			try {
				List<String> relatedTypesNames = new ArrayList<String>();
				List<String> resolvedTokens = new ArrayList<String>();

				IJavaProject project = element.getJavaProject();

				String source = element.getSource();

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
							String[][] resolveType = element.resolveType(currentToken);
							if (resolveType != null) {
								String elementName = resolveType[0][0] + "." + resolveType[0][1];
								relatedTypesNames.add(elementName);
							}
							resolvedTokens.add(currentToken);
						}
					}
				}
				
				for (String relatedTypeName : relatedTypesNames) {
					IType type = project.findType(relatedTypeName);
					if (type != null && !type.isBinary()) {
						TypeClassModelGenerator generator = new TypeClassModelGenerator(type);
						Container relatedContainer = generator.analize(false);
						createdContainer.addRelatedContainer(relatedContainer);
					}
				}				
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
		}
	}

}