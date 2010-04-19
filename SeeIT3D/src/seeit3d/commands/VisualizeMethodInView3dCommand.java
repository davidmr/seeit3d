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
package seeit3d.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.creator.MethodModelCreator;

/**
 * Concrete implementation of <code>AbstracVisualizaJavaElement</code> to show methods in the visualization area
 * 
 * @author David Monta�o
 * 
 */
public class VisualizeMethodInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		return new MethodModelCreator((IMethod) javaElement);
	}

}
