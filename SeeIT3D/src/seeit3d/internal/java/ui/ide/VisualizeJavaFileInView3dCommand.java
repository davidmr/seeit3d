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
package seeit3d.internal.java.ui.ide;

import org.eclipse.jdt.core.ICompilationUnit;

import seeit3d.internal.java.JavaConstants;

/**
 * Concrete implementation of <code>AbstracVisualizaJavaElement</code> to show Java files in the visualization area
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJavaFileInView3dCommand extends AbstractVisualizeJavaElement<ICompilationUnit> {

	@Override
	protected String getModelProviderKey() {
		return JavaConstants.MODEL_PROVIDER_KEY_TYPE;
	}

}
