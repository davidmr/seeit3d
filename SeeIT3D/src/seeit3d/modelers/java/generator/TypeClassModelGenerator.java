/**
 * Copyright (C) 2010  David Monta�o
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

import java.util.List;

import org.eclipse.jdt.core.*;

import seeit3d.general.model.generator.IModelGenerator;
import seeit3d.modelers.java.generator.metrics.ComplexityCalculator;
import seeit3d.modelers.java.generator.metrics.LOCCalculator;

import com.google.common.collect.Lists;

/**
 * Class/Type analyzer @see AbstracModelGenerator
 * 
 * @author David Monta�o
 * 
 */
public class TypeClassModelGenerator extends AbstracModelGenerator<IType, IMethod> {

	public TypeClassModelGenerator(IType elementToAnalize) {
		super(elementToAnalize);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ComplexityCalculator.name);
	}

	@Override
	protected IMethod[] fetchChildren(IType element) throws JavaModelException {
		return element.getMethods();
	}

	@Override
	protected IModelGenerator lowerLevelModelGenerator(IMethod childrenElement) {
		return new MethodModelGenerator(childrenElement);
	}

}