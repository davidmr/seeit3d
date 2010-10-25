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
package seeit3d.xml.ui.ide;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.base.error.ErrorHandler;
import seeit3d.base.error.exception.SeeIT3DXMLParsingException;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.ui.ide.commands.jobs.VisualizeJob;
import seeit3d.xml.XMLContribution;
import seeit3d.xml.modeler.annotation.XMLModeler;
import seeit3d.xml.modeler.internal.Container;
import seeit3d.xml.modeler.internal.Containers;

import com.google.inject.Inject;

/**
 * Command to read and visualize the information contained in a XML file which conforms to the specification at package <code>seeit3d.model.xml.xsd</code>
 * 
 * @author David Montaño
 * 
 */
public class VisualizeFromXMLFile extends AbstractHandler {

	private IModelGenerator<Container> modelGenerator;

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XMLContribution.injector().injectMembers(this);

		IStructuredSelection currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Iterator<IFile> iterator = currentSelection.iterator();
		IFile file = iterator.next();
		try {
			InputStream contents = file.getContents();

			JAXBContext context = JAXBContext.newInstance("seeit3d.xml.modeler.internal");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Containers containers = (Containers) unmarshaller.unmarshal(contents);
			Shell shell = HandlerUtil.getActiveShell(event);
			VisualizeJob job = new VisualizeJob(shell, modelGenerator, containers.getContainer());
			job.schedule();
		} catch (CoreException e) {
			ErrorHandler.error(e);
		} catch (JAXBException e) {
			ErrorHandler.error(new SeeIT3DXMLParsingException(e));
		}
		return null;
	}

	@Inject
	public void setModelGenerator(@XMLModeler IModelGenerator<Container> modelGenerator) {
		this.modelGenerator = modelGenerator;
	}

}
