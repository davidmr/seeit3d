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
package seeit3d.internal.base.error;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.internal.base.error.exception.SeeIT3DException;

/**
 * Error handler class. Presents the user and information dialog with the specific error on it
 * 
 * @author David Montaño
 * 
 */
public class ErrorHandler {

	private static Shell shell;

	public static void setShell(Shell shell) {
		ErrorHandler.shell = shell;
	}

	public static void error(final SeeIT3DException exception) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(shell, "SeeIT 3D message", exception.getMessage());
				exception.printStackTrace();
			}
		});

	}

	public static void error(final Exception exception) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(shell, "Unexpected internal error", "An unexpected error has ocurred. \n" + exception.getMessage());
				exception.printStackTrace();
			}
		});

	}

	public static void error(final String errorText) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(shell, "SeeIT 3D message", errorText);
			}
		});

	}

}
