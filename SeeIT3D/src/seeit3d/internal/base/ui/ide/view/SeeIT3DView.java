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
package seeit3d.internal.base.ui.ide.view;

import static seeit3d.internal.base.bus.EventBus.registerListener;
import static seeit3d.internal.base.bus.EventBus.unregisterListener;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.expressions.ActivePartExpression;
import org.eclipse.ui.part.ViewPart;

import seeit3d.internal.SeeIT3D;
import seeit3d.internal.base.bus.events.MappingViewNeedsUpdateEvent;
import seeit3d.internal.base.bus.events.SelectedInformationChangedEvent;
import seeit3d.internal.base.core.api.ISeeIT3DCore;
import seeit3d.internal.base.ui.ide.view.listeners.LabelInformation;

import com.google.inject.Inject;

/**
 * This class creates and initializes the seeit3d view
 * 
 * @author David Montaño
 * 
 */
@SuppressWarnings("restriction")
public class SeeIT3DView extends ViewPart {

	public static final String ID = "seeit3d.seeit3dview";

	private ISeeIT3DCore seeIT3DCore;

	private LabelInformation infoLabel;

	@Override
	public void createPartControl(Composite parent) {

		SeeIT3D.injector().injectMembers(this);
		IContextService contextService = (IContextService) getSite().getService(IContextService.class);
		contextService.activateContext("seeit3d.seeit3dviewContext", new ActivePartExpression(getSite().getPart()));

		GridLayout viewLayout = new GridLayout(1, true);
		viewLayout.verticalSpacing = 0;
		parent.setLayout(viewLayout);

		StyledText text = new StyledText(parent, SWT.READ_ONLY | SWT.WRAP);
		text.setBackground(parent.getBackground());
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.heightHint = 30;
		text.setLayoutData(labelData);

		infoLabel = new LabelInformation(text);
		registerListener(SelectedInformationChangedEvent.class, infoLabel);
		
		Sash sashInformationSize = new Sash(parent, SWT.HORIZONTAL);
		GridData sashInfoLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		sashInformationSize.setLayoutData(sashInfoLayoutData);
		sashInformationSize.addListener(SWT.Selection, new ResizeListener(text, false));

		Composite visualizationComposite = new Composite(parent, SWT.EMBEDDED);

		GridData data = new GridData(GridData.FILL_BOTH);
		visualizationComposite.setLayoutData(data);

		Frame glFrame = SWT_AWT.new_Frame(visualizationComposite);
		glFrame.add(seeIT3DCore.getCanvas());
		
		Sash sashMappingViewSize = new Sash(parent, SWT.HORIZONTAL);

		GridData sashMappingLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		sashMappingViewSize.setLayoutData(sashMappingLayoutData);

		MappingViewComposite mappingComposite = new MappingViewComposite(parent);
		GridData mappingCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		mappingCompositeData.heightHint = 180;
		mappingComposite.setLayoutData(mappingCompositeData);

		sashMappingViewSize.addListener(SWT.Selection, new ResizeListener(mappingComposite, true));

		registerListener(MappingViewNeedsUpdateEvent.class, mappingComposite);

	}

	@Override
	public void setFocus() {}

	@Override
	public void dispose() {
		super.dispose();
		unregisterListener(SelectedInformationChangedEvent.class, infoLabel);
	}

	@Inject
	public void setSeeIT3DCore(ISeeIT3DCore seeIT3DCore) {
		this.seeIT3DCore = seeIT3DCore;
	}

}
