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
package seeit3d.internal.base.core.handler;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.internal.base.core.api.IVisualizationStateChecker;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.model.Container;

import com.google.inject.Singleton;

/**
 * This class is responsible for validating if the visualization state is valid and can be processed
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultVisualizationStateChecker implements IVisualizationStateChecker {

	private static final int CONTAINERS_THRESHOLD = 20;

	private static final int POLYCYLINDERS_THRESHOLD = 2000;
	
	private volatile Boolean answer;

	@Override
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
