package seeit3d.view;

import java.awt.Frame;
import java.awt.GraphicsConfiguration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.expressions.ActivePartExpression;
import org.eclipse.ui.part.ViewPart;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.view.listeners.LabelInformation;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * This class creates the seeit3d view
 * 
 * @author David
 * 
 */
@SuppressWarnings("restriction")
public class SeeIT3DView extends ViewPart {

	public static final String ID = "seeit3d.seeit3dview";

	private final SeeIT3DManager manager;

	private SeeIT3DCanvas canvas;

	public SeeIT3DView() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void createPartControl(Composite parent) {

		IContextService contextService = (IContextService) getSite().getService(IContextService.class);
		contextService.activateContext("seeit3d.seeit3dviewContext", new ActivePartExpression(getSite().getPart()));

		GridLayout viewLayout = new GridLayout(1, true);
		parent.setLayout(viewLayout);

		Label label = new Label(parent, SWT.LEFT);
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.heightHint = 30;
		label.setLayoutData(labelData);

		LabelInformation infoLabel = new LabelInformation(label);
		manager.registerSelectionInformatioAware(infoLabel);

		Composite visualizationComposite = new Composite(parent, SWT.EMBEDDED);

		GridData data = new GridData(GridData.FILL_BOTH);
		visualizationComposite.setLayoutData(data);

		Frame glFrame = SWT_AWT.new_Frame(visualizationComposite);
		GraphicsConfiguration conf = SimpleUniverse.getPreferredConfiguration();
		canvas = new SeeIT3DCanvas(conf);
		glFrame.add(canvas);

		MappingViewComposite mappingComposite = new MappingViewComposite(parent);
		GridData mappingCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		mappingCompositeData.heightHint = 165;
		mappingComposite.setLayoutData(mappingCompositeData);

		manager.registerMappingView(mappingComposite);
		manager.initializeVisualization(canvas);

	}

	@Override
	public void setFocus() {
	}

}
