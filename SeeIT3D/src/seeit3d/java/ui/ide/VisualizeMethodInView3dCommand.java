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
package seeit3d.java.ui.ide;

import org.eclipse.jdt.core.IMethod;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.java.modeler.generator.annotation.MethodModeler;

import com.google.inject.Inject;

/**
 * Concrete implementation of <code>AbstracVisualizaJavaElement</code> to show methods in the visualization area
 * 
 * @author David Montaño
 * 
 */
public class VisualizeMethodInView3dCommand extends AbstractVisualizeJavaElement<IMethod> {

	@Override
	@Inject
	protected void setModelGenerator(@MethodModeler IModelGenerator<IMethod> modelGenerator) {
		this.modelGenerator = modelGenerator;
	}

}
