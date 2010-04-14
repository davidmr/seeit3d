package seeit3d.error;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import seeit3d.error.exception.SeeIT3DException;

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
