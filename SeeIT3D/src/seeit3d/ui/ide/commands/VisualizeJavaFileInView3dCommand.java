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
package seeit3d.ui.ide.commands;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import seeit3d.core.model.generator.IModelGenerator;
import seeit3d.modelers.java.generator.TypeClassModelCreator;

/**
 * Concrete implementation of <code>AbstracVisualizaJavaElement</code> to show Java files in the visualization area
 * 
 * @author David Monta�o
 * 
 */
public class VisualizeJavaFileInView3dCommand extends AbstractVisualizeJavaElement {
	@Override
	protected IModelGenerator createModel(IJavaElement javaElement) {
		ICompilationUnit unit = (ICompilationUnit) javaElement;
		IModelGenerator creator = new TypeClassModelCreator(unit.findPrimaryType());
		return creator;
	}

}
