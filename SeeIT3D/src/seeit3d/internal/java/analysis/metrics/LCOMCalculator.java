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
package seeit3d.internal.java.analysis.metrics;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;

import seeit3d.analysis.metric.AbstractNumericMetricCalculator;

/**
 * Lack of Cohesion (3) calculator.
 * 
 * @author David Montaño
 * 
 */
public class LCOMCalculator extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = 1L;

	public static final LCOMCalculator INSTANCE = new LCOMCalculator();

	public static final String name = "Lack of Cohesion";

	private LCOMCalculator() {
		super(name);
	}

	@Override
	public Float calculateNumericValue(Object element) {
		try {
			if (element instanceof ICompilationUnit) {
				IType primaryType = ((ICompilationUnit) element).findPrimaryType();
				if (primaryType != null) {
					float numberMethods = primaryType.getMethods().length;
					float numberAttributes = primaryType.getFields().length;

					if (numberAttributes <= 1f || numberMethods <= 1f) {
						return DEFAULT_VALUE;
					}

					float sumAttributesUsedInMethods = 0;
					for (int i = 0; i < primaryType.getFields().length; i++) {
						IField field = primaryType.getFields()[i];
						int methodsThatUseTheField = calculateMethodsThatUseTheField(field, primaryType.getMethods());
						sumAttributesUsedInMethods += methodsThatUseTheField;
					}

					float value = (numberMethods - (sumAttributesUsedInMethods / numberAttributes)) / (numberMethods - 1);

					return value;

				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return DEFAULT_VALUE;
	}

	private int calculateMethodsThatUseTheField(IField field, IMethod[] methods) throws JavaModelException {
		int methodsThatUse = 0;
		for (IMethod method : methods) {
			if (methodUseField(field, method)) {
				methodsThatUse++;
			}
		}
		return methodsThatUse;
	}

	private boolean methodUseField(IField field, IMethod method) throws JavaModelException {
		try {
			String fieldName = field.getElementName();
			String methodSource = method.getSource();

			IScanner scanner = ToolFactory.createScanner(false, false, false, false);
			scanner.setSource(methodSource.toCharArray());
			while (true) {
				int token = scanner.getNextToken();
				if (token == ITerminalSymbols.TokenNameEOF)
					break;
				if (token == ITerminalSymbols.TokenNameIdentifier) {
					String identifier = new String(scanner.getCurrentTokenSource());
					if (identifier.equals(fieldName)) {
						return true;
					}
				}
			}
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		return false;
	}

}
