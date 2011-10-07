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
package seeit3d.internal.xml.ui.ide;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.error.exception.SeeIT3DXMLParsingException;
import seeit3d.internal.xml.XMLContribution;
import seeit3d.internal.xml.internal.Containers;
import seeit3d.jobs.VisualizeJob;

/**
 * Command to read and visualize the information contained in a XML file which conforms to the specification at package <code>seeit3d.internal.xml.internal.xsd</code>
 * 
 * @author David Montaño
 * 
 */
public class VisualizeFromXMLFile extends AbstractHandler {


	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IStructuredSelection currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Iterator<IFile> iterator = currentSelection.iterator();
		IFile file = iterator.next();
		try {
			InputStream contents = file.getContents();
			JAXBContext context = JAXBContext.newInstance("seeit3d.internal.xml.internal");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Containers containers = (Containers) unmarshaller.unmarshal(contents);
			Shell shell = HandlerUtil.getActiveShell(event);
			VisualizeJob job = new VisualizeJob(shell, XMLContribution.MODEL_PROVIDER_KEY_XML, containers.getContainer());
			job.schedule();
		} catch (CoreException e) {
			ErrorHandler.error(e);
		} catch (JAXBException e) {
			ErrorHandler.error(new SeeIT3DXMLParsingException(e));
		}
		return null;
	}


}
