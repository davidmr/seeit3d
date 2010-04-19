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
package seeit3d.commands;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.commands.jobs.VisualizeJob;
import seeit3d.model.IModelCreator;
import seeit3d.model.NoOpCreator;
import seeit3d.model.java.creator.*;

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
		IModelCreator modelCreator = createModel(parameter, javaElement);
		VisualizeJob visualizeJob = new VisualizeJob(shell, Lists.newArrayList(modelCreator));
		visualizeJob.schedule();
		return null;
	}

	private IModelCreator createModel(String parameter, ICompilationUnit element) {
		if (parameter.equals("JAVA_FILE")) {
			return new TypeClassModelCreator(element.findPrimaryType());
		} else if (parameter.equals("PACKAGE")) {
			IPackageFragment packageFragment = element.findPrimaryType().getPackageFragment();
			return new PackageModelCreator(packageFragment);
		} else if (parameter.equals("PROJECT")) {
			return new ProjectModelCreator(element.getJavaProject());
		} else {
			return new NoOpCreator();
		}
	}

}
