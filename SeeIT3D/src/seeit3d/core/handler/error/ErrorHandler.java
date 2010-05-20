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
package seeit3d.core.handler.error;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import seeit3d.core.handler.error.exception.SeeIT3DException;

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

	public static void error(SeeIT3DException exception) {
		MessageDialog.openError(shell, "SeeIT 3D message", exception.getMessage());
		exception.printStackTrace();
	}

	public static void error(Exception exception) {
		MessageDialog.openError(shell, "Unexpected internal error", "An unexpected error has ocurred. \n" + exception.getMessage());
		exception.printStackTrace();
	}

	public static void error(String errorText) {
		MessageDialog.openError(shell, "SeeIT 3D message", errorText);
	}

}
