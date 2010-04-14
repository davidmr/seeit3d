package seeit3d.commands;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import seeit3d.commands.jobs.VisualizeJob;
import seeit3d.error.ErrorHandler;
import seeit3d.error.exception.SeeIT3DXMLParsingException;
import seeit3d.model.IModelCreator;
import seeit3d.model.xml.SeeIT3DSchema;
import seeit3d.model.xml.XMLBasedModelCreator;
import seeit3d.model.xml.internal.Container;
import seeit3d.model.xml.internal.Containers;

public class VisualizeFromXMLFile extends AbstractHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection currentSelection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Iterator<IFile> iterator = currentSelection.iterator();
		IFile file = iterator.next();
		try {
			InputStream contents = file.getContents();

			JAXBContext context = JAXBContext.newInstance(Containers.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(SeeIT3DSchema.getSchema());
			Containers containers = (Containers) unmarshaller.unmarshal(contents);
			List<IModelCreator> modelCreators = new ArrayList<IModelCreator>();
			for (Container containerXML : containers.getContainer()) {
				modelCreators.add(new XMLBasedModelCreator(containerXML));
			}
			Shell shell = HandlerUtil.getActiveShell(event);
			VisualizeJob job = new VisualizeJob(shell, modelCreators);
			job.schedule();
		} catch (CoreException e) {
			ErrorHandler.error(e);
		} catch (JAXBException e) {
			ErrorHandler.error(new SeeIT3DXMLParsingException(e));
		}
		return null;
	}

}
