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
package seeit3d.ui.ide.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;

import seeit3d.core.model.generator.IModelGenerator;
import seeit3d.core.model.utils.NoOpModelGenerator;
import seeit3d.modelers.java.generator.PackageModelCreator;

/**
 * Concrete implementation of <code>AbstracVisualizaJavaElement</code> to show packages in the visualization area
 * 
 * @author David Montaño
 * 
 */
public class VisualizePackageInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelGenerator createModel(IJavaElement javaElement) {
		IJavaElement packageToAnalyze = (IPackageFragment) javaElement;
		if (!packageToAnalyze.isReadOnly()) {
			return new PackageModelCreator((IPackageFragment) packageToAnalyze);
		} else {
			return new NoOpModelGenerator();
		}

	}

}
