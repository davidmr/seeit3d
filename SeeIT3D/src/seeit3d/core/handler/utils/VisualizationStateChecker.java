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
package seeit3d.core.handler.utils;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.Container;

/**
 * This class is responsible for validating if the visualization state is valid and can be processed
 * 
 * @author David Montaño
 * 
 */
public class VisualizationStateChecker {

	private static final int CONTAINERS_THRESHOLD = 20;

	private static final int POLYCYLINDERS_THRESHOLD = 2000;
	
	private volatile Boolean answer;

	public boolean checkState(List<Container> containers) {
		boolean askNeeded = false;
		if (containers.size() > CONTAINERS_THRESHOLD) {
			askNeeded = true;
		}
		int polycylinders = 0;
		for (Container container : containers) {
			polycylinders += container.countPolyCylinders();
		}
		if (polycylinders > POLYCYLINDERS_THRESHOLD) {
			askNeeded = true;
		}

		if (askNeeded) {
			answer = null;
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					Shell shell = new Shell(Display.getCurrent());					
					boolean confirm = MessageDialog.open(MessageDialog.CONFIRM, shell, "Possibly too much information", "Too much information is about to be visualized, it could block your computer.\nAre you sure you want to continue?", SWT.NONE);
					answer = new Boolean(confirm);
				}
			});
			
			while(answer == null){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					ErrorHandler.error(e);
				}
			}			
			return answer;
		} else {
			return true;
		}
	}

}
