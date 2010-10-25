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

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.ui.ide.commands.jobs.VisualizeJob;
import seeit3d.java.JavaContribution;

/**
 * Basic algorithm to visualize IJavaElements from different views in the IDE.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractVisualizeJavaElement<T extends IJavaElement> extends AbstractHandler {

	protected IModelGenerator<T> modelGenerator;

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		JavaContribution.injector().injectMembers(this);
		List<T> elementsToAnalize = new ArrayList<T>();
		ITreeSelection currentSelection = (ITreeSelection) HandlerUtil.getCurrentSelection(event);
		for (Iterator<Object> iterator = currentSelection.iterator(); iterator.hasNext();) {
			T javaElement = (T) iterator.next();
			elementsToAnalize.add(javaElement);
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		VisualizeJob visualizeJob = new VisualizeJob(shell, modelGenerator, elementsToAnalize);
		visualizeJob.schedule();
		return null;
	}

	protected abstract void setModelGenerator(IModelGenerator<T> modelGenerator);

}