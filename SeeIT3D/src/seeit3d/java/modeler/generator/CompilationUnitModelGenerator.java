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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;

import seeit3d.base.model.Container;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.java.modeler.generator.annotation.TypeModeler;
import seeit3d.utils.Utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * CompilationUnit analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class CompilationUnitModelGenerator implements IModelGenerator<ICompilationUnit> {

	private final IModelGenerator<IType> generator;

	@Inject
	public CompilationUnitModelGenerator(@TypeModeler IModelGenerator<IType> generator) {
		this.generator = generator;
	}

	@Override
	public Container analize(ICompilationUnit element, boolean includeDependecies) {
		IType primaryType = element.findPrimaryType();
		if (primaryType != null) {
			return generator.analize(primaryType, includeDependecies);
		}
		return Utils.emptyContainer();
	}

}