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
package seeit3d.internal.base.ui.ide.commands;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import seeit3d.internal.base.bus.events.PerformOperationOnSelectedPolycylindersEvent;
import seeit3d.internal.base.ui.actions.ChangeTransparencyFunction;

/**
 * Base command to change transparency on polycylinders
 * 
 * @author David Montaño
 * 
 */
public abstract class ChangeTransparencyBaseCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ChangeTransparencyFunction function = new ChangeTransparencyFunction(moreTransparent());
		PerformOperationOnSelectedPolycylindersEvent operation = new PerformOperationOnSelectedPolycylindersEvent(function, false);
		publishEvent(operation);
		return null;
	}

	protected abstract boolean moreTransparent();

}
