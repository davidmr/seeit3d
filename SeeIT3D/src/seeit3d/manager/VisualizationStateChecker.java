package seeit3d.manager;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.error.ErrorHandler;
import seeit3d.model.representation.Container;

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
