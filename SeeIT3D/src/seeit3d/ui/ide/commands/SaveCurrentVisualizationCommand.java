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

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.ui.ide.commands.jobs.SaveVisualizationJob;

/**
 * Command to allow the user to save the visualization state
 * 
 * @author David Montaño
 * 
 */
public class SaveCurrentVisualizationCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell shell = HandlerUtil.getActiveShell(event);

		FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
		saveDialog.setFileName("visualization");
		saveDialog.setOverwrite(true);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String workspaceLocation = root.getLocation().makeAbsolute().toOSString();
		saveDialog.setFilterPath(workspaceLocation);
		saveDialog.setFilterExtensions(new String[] { "*.s3d" });
		String filename = saveDialog.open();
		if (filename != null) {
			SaveVisualizationJob save = new SaveVisualizationJob(filename);
			save.schedule();
		}
		return null;
	}
}
