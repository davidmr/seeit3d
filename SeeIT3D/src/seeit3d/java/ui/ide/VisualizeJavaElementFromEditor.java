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

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.ui.ide.commands.jobs.VisualizeJob;
import seeit3d.java.JavaContribution;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Command to allow the user to visualize information when he/she is using the Java Editor
 * 
 * @author David Montaño
 * 
 */
// TODO check if can be better written
public class VisualizeJavaElementFromEditor extends AbstractHandler {

	private static final String ELEMENT_TO_VISUALIZE = "seeit3d.visualizeFromEditorParameter";

	public Map<String, Provider<? extends IModelGenerator<? extends IJavaElement>>> modelMap;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		JavaContribution.injector().injectMembers(this);
		final String parameter = event.getParameter(ELEMENT_TO_VISUALIZE);
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		final ICompilationUnit javaElement = (ICompilationUnit) JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());
		final Shell shell = HandlerUtil.getActiveShell(event);
		IModelGenerator<IJavaElement> modelGenerator = getModelGenerator(parameter);
		IJavaElement elementToAnalize = elementToAnalize(parameter, javaElement);
		List<IJavaElement> objects = Lists.newArrayList(elementToAnalize);
		VisualizeJob<? extends IJavaElement> visualizeJob = new VisualizeJob<IJavaElement>(shell, modelGenerator, objects);
		visualizeJob.schedule();
		return null;
	}

	@SuppressWarnings("unchecked")
	private IModelGenerator<IJavaElement> getModelGenerator(String parameter) {
		Provider<? extends IModelGenerator<? extends IJavaElement>> provider = modelMap.get(parameter);
		return (IModelGenerator<IJavaElement>) provider.get();
	}

	@Inject
	public void setModelMap(@Named(value = "modelMap") Map<String, Provider<? extends IModelGenerator<? extends IJavaElement>>> modelMap) {
		this.modelMap = modelMap;
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
