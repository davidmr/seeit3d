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
package seeit3d.java.modeler.generator;

import static com.google.common.collect.Lists.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import seeit3d.base.model.Container;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.generator.annotation.MethodModeler;
import seeit3d.java.modeler.generator.metrics.annotation.LOC;
import seeit3d.java.modeler.generator.metrics.annotation.McCabeComplexity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class/Type analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class TypeClassModelGenerator extends AbstracModelGenerator<IType, IMethod> {

	private final List<MetricCalculator> metrics;

	@Inject
	public TypeClassModelGenerator(
			@MethodModeler IModelGenerator<IMethod> lowerLevelModelGenerator,
			@LOC MetricCalculator loc,
			@McCabeComplexity MetricCalculator mccabe) {
		super(lowerLevelModelGenerator);
		this.metrics = newArrayList(loc, mccabe);
	}

	@Override
	protected List<MetricCalculator> getMetrics() {
		return metrics;

	}

	@Override
	protected IMethod[] fetchChildren(IType element) throws JavaModelException {
		return element.getMethods();
	}

	@Override
	protected void extraOperationsOnContainer(Container createdContainer, IType element, boolean analizeDependencies) throws JavaModelException {
		if (analizeDependencies) {
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
						Container relatedContainer = this.analize(type, false);
						createdContainer.addRelatedContainer(relatedContainer);
					}
				}
			} catch (InvalidInputException e) {
				e.printStackTrace();
			}
		}
	}

}