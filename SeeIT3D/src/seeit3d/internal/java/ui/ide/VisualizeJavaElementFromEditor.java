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

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.internal.java.JavaConstants;
import seeit3d.jobs.VisualizeJob;

import com.google.common.collect.Lists;

/**
 * Command to allow the user to visualize information when he/she is using the Java Editor
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJavaElementFromEditor extends AbstractHandler {

	private static final String ELEMENT_TO_VISUALIZE = "seeit3d.visualizeFromEditorParameter";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final String parameter = event.getParameter(ELEMENT_TO_VISUALIZE);
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		final ICompilationUnit javaElement = (ICompilationUnit) JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());
		final Shell shell = HandlerUtil.getActiveShell(event);
		
		IJavaElement elementToAnalize = elementToAnalize(parameter, javaElement);
		String modelProviderKey = modelProviderkey(parameter);
		
		List<IJavaElement> objects = Lists.newArrayList(elementToAnalize);
		
		VisualizeJob visualizeJob = new VisualizeJob(shell, modelProviderKey, objects);
		visualizeJob.schedule();
		return null;
	}

	private String modelProviderkey(String parameter) {
		if(parameter.equals(JavaConstants.JAVA_FILE)){
			return JavaConstants.MODEL_PROVIDER_KEY_TYPE;
		}
		if(parameter.equals(JavaConstants.PACKAGE)){
			return JavaConstants.MODEL_PROVIDER_KEY_PACKAGE;
		}
		if(parameter.equals(JavaConstants.PROJECT)){
			return JavaConstants.MODEL_PROVIDER_KEY_PROJECT;
		}
		return null;
	}

	private IJavaElement elementToAnalize(String parameter, ICompilationUnit javaElement) {
		if (parameter.equals("JAVA_FILE")) {
			return javaElement;
		} else if (parameter.equals("PACKAGE")) {
			if (javaElement.findPrimaryType() != null) {
				return javaElement.findPrimaryType().getPackageFragment();
			}
		} else if (parameter.equals("PROJECT")) {
			return javaElement.getJavaProject();
		}
		return null;
	}

}
