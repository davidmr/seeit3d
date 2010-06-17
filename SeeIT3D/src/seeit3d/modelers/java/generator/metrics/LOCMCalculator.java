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
package seeit3d.modelers.java.generator.metrics;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.*;

import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;
import seeit3d.general.model.generator.metrics.AbstractContinuousMetricCalculator;

/**
 * Lack of Cohesion (3) calculator.
 * 
 * @author David Montaño
 * 
 */
@SeeIT3DFactoryEnabled(singleton = true)
public class LOCMCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = 1L;

	public static final String name = "Lack of Cohesion";

	public LOCMCalculator() {
		super(name);
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
		try {
			if (element instanceof ICompilationUnit) {
				IType primaryType = ((ICompilationUnit) element).findPrimaryType();
				float numberMethods = primaryType.getMethods().length;
				float numberAttributes = primaryType.getFields().length;

				if(numberAttributes <= 1f || numberMethods <= 1f){
					return DEFAULT_VALUE;
				}
				
				float sumAttributesUsedInMethods = 0;
				for (int i = 0; i < primaryType.getFields().length; i++) {
					IField field = primaryType.getFields()[i];
					int methodsThatUseTheField = calculateMethodsThatUseTheField(field, primaryType.getMethods());
					sumAttributesUsedInMethods += methodsThatUseTheField;
				}

				float value = (numberMethods - (sumAttributesUsedInMethods / numberAttributes)) / (numberMethods - 1);

				return String.valueOf(value);
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

	@Override
	public float getMaxValue() {
		return 2;
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

}
