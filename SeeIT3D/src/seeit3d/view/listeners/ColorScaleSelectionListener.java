package seeit3d.view.listeners;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import seeit3d.colorscale.IColorScale;
import seeit3d.manager.SeeIT3DManager;

public class ColorScaleSelectionListener implements SelectionListener {

	private final SeeIT3DManager manager;

	private final List<IColorScale> colorScales;

	public ColorScaleSelectionListener(List<IColorScale> colorScales) {
		this.colorScales = colorScales;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Combo combo = (Combo) event.widget;
		String colorScaleName = combo.getItem(combo.getSelectionIndex());

		for (IColorScale colorScale : colorScales) {
			if (colorScaleName.equals(colorScale.getName())) {
				manager.setColorScale(colorScale);
				manager.refreshVisualization();
				break;
			}
		}
	}

}
