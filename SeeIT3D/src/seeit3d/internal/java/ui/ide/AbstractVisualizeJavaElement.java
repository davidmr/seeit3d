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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.jobs.VisualizeJob;

/**
 * Basic algorithm to visualize IJavaElements from different views in the IDE.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractVisualizeJavaElement<T extends IJavaElement> extends AbstractHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		List<T> elementsToAnalize = new ArrayList<T>();
		ITreeSelection currentSelection = (ITreeSelection) HandlerUtil.getCurrentSelection(event);
		for (Iterator<Object> iterator = currentSelection.iterator(); iterator.hasNext();) {
			T javaElement = (T) iterator.next();
			elementsToAnalize.add(javaElement);
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		String modelProviderKey = getModelProviderKey();
		VisualizeJob visualizeJob = new VisualizeJob(shell, modelProviderKey, elementsToAnalize);
		visualizeJob.schedule();
		return null;
	}

	protected abstract String getModelProviderKey();

}
