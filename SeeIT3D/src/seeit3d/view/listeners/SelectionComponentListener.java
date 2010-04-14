package seeit3d.view.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

import seeit3d.manager.SeeIT3DManager;

public class SelectionComponentListener implements SelectionListener {

	public static final String COMPONENT_LEVEL_DETAIL = "component_level_detail";

	private final SeeIT3DManager manager;

	public SelectionComponentListener() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Button source = (Button) event.getSource();
		ChangeLevelOption option = (ChangeLevelOption) source.getData(COMPONENT_LEVEL_DETAIL);
		manager.updateViewUsingLevelOnSelectedContainer(option.moreDetail);
	}
}
