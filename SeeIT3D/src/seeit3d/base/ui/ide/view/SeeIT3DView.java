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
package seeit3d.base.ui.ide.view;

import static seeit3d.base.bus.EventBus.*;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.expressions.ActivePartExpression;
import org.eclipse.ui.part.ViewPart;

import seeit3d.base.SeeIT3D;
import seeit3d.base.bus.events.MappingViewNeedsUpdateEvent;
import seeit3d.base.bus.events.SelectedInformationChangedEvent;
import seeit3d.base.core.api.ISeeIT3DCore;
import seeit3d.base.ui.ide.view.listeners.LabelInformation;

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

	@Override
	public void createPartControl(Composite parent) {
		SeeIT3D.injector().injectMembers(this);
		IContextService contextService = (IContextService) getSite().getService(IContextService.class);
		contextService.activateContext("seeit3d.seeit3dviewContext", new ActivePartExpression(getSite().getPart()));

		GridLayout viewLayout = new GridLayout(1, true);
		parent.setLayout(viewLayout);

		Label label = new Label(parent, SWT.LEFT);
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.heightHint = 30;
		label.setLayoutData(labelData);

		LabelInformation infoLabel = new LabelInformation(label);
		registerListener(SelectedInformationChangedEvent.class, infoLabel);

		Composite visualizationComposite = new Composite(parent, SWT.EMBEDDED);

		GridData data = new GridData(GridData.FILL_BOTH);
		visualizationComposite.setLayoutData(data);

		Frame glFrame = SWT_AWT.new_Frame(visualizationComposite);
		glFrame.add(seeIT3DCore.getCanvas());

		MappingViewComposite mappingComposite = new MappingViewComposite(parent);
		GridData mappingCompositeData = new GridData(GridData.FILL_HORIZONTAL);
		mappingCompositeData.heightHint = 180;
		mappingComposite.setLayoutData(mappingCompositeData);

		registerListener(MappingViewNeedsUpdateEvent.class, mappingComposite);

	}

	@Override
	public void setFocus() {}

	@Inject
	public void setSeeIT3DCore(ISeeIT3DCore seeIT3DCore) {
		this.seeIT3DCore = seeIT3DCore;
	}

}
