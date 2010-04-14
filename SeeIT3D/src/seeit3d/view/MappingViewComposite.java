package seeit3d.view;

import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import seeit3d.colorscale.ColorScaleFactory;
import seeit3d.colorscale.IColorScale;
import seeit3d.manager.IMappingView;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.model.representation.Container;
import seeit3d.model.representation.VisualProperty;
import seeit3d.utils.DragAndDropHelper;
import seeit3d.view.dnd.DropMetricOnMetricContainerListener;
import seeit3d.view.dnd.DropMetricOnVisualPropertyListener;
import seeit3d.view.listeners.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MappingViewComposite extends Composite implements IMappingView {

	public static final String VISUAL_PROPERTY = "visualProperty";

	private Composite rootComposite;

	private ScrolledComposite scrollerComposite;

	public MappingViewComposite(Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.setLayout(new FillLayout());
		updateComponents(new ArrayList<Container>());
	}

	private void updateComponents(List<Container> containers) {
		cleanComposites();

		scrollerComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.DOUBLE_BUFFERED);
		rootComposite = new Composite(scrollerComposite, SWT.DOUBLE_BUFFERED);
		scrollerComposite.setContent(rootComposite);

		GridLayout mappingLayout = new GridLayout(2 + VisualProperty.values().length, true);
		rootComposite.setLayout(mappingLayout);

		updateCurrentGranularityLevelFromContainers(containers);

		List<BaseMetricCalculator> metricsInformation = buildMetricsInformation(containers);
		BiMap<BaseMetricCalculator, VisualProperty> currentMapping = buildCurrentMapping(containers);

		removeAlreadyMappedMetrics(metricsInformation, currentMapping);
		updateCurrentMappingAndVisualProperties(containers, currentMapping);
		updateColorScales();
		updateMetricsFromContainers(containers, metricsInformation, currentMapping.size());

		rootComposite.pack(true);
		this.layout(true, true);

	}

	private void cleanComposites() {
		if (rootComposite != null) {
			rootComposite.dispose();
		}
		if (scrollerComposite != null) {
			scrollerComposite.dispose();
		}
	}

	private void removeAlreadyMappedMetrics(List<BaseMetricCalculator> metricsInformation, BiMap<BaseMetricCalculator, VisualProperty> currentMapping) {
		for (Iterator<BaseMetricCalculator> iterator = metricsInformation.iterator(); iterator.hasNext();) {
			BaseMetricCalculator metric = iterator.next();
			if (currentMapping.containsKey(metric)) {
				iterator.remove();
			}
		}
	}

	private void updateCurrentGranularityLevelFromContainers(List<Container> currentContainers) {

		String currentGranularityLevelFromContainers = getCurrentGranularityLevelFromContainers(currentContainers);
		SelectionComponentListener selectionComponentListener = new SelectionComponentListener();
		Group componentSelectionGroup = new Group(rootComposite, SWT.SHADOW_OUT);
		GridData componetLayoutData = new GridData(GridData.CENTER);
		componetLayoutData.verticalSpan = 2;
		componetLayoutData.widthHint = 150;
		componentSelectionGroup.setLayoutData(componetLayoutData);
		componentSelectionGroup.setText("Component Level");
		GridLayout groupLayout = new GridLayout(3, false);
		componentSelectionGroup.setLayout(groupLayout);
		Button previousLevelButton = new Button(componentSelectionGroup, SWT.ARROW | SWT.LEFT);
		previousLevelButton.setData(SelectionComponentListener.COMPONENT_LEVEL_DETAIL, ChangeLevelOption.PREVIOUS_LEVEL);
		previousLevelButton.addSelectionListener(selectionComponentListener);
		Label currentComponent = new Label(componentSelectionGroup, SWT.CENTER);
		GridData currentComponentLayoutData = new GridData(GridData.CENTER | GridData.FILL_HORIZONTAL);
		currentComponent.setLayoutData(currentComponentLayoutData);
		currentComponent.setToolTipText("This is the current level of component selected");
		currentComponent.setText(currentGranularityLevelFromContainers);
		Button nextLevelButton = new Button(componentSelectionGroup, SWT.ARROW | SWT.RIGHT);
		nextLevelButton.addSelectionListener(selectionComponentListener);
		nextLevelButton.setData(SelectionComponentListener.COMPONENT_LEVEL_DETAIL, ChangeLevelOption.NEXT_LEVEL);

	}

	private String getCurrentGranularityLevelFromContainers(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			String level = currentContainers.get(0).getGranularityLevelName();
			for (int i = 1; i < currentContainers.size(); i++) {
				String otherLevel = currentContainers.get(i).getGranularityLevelName();
				if (!level.equals(otherLevel)) {
					return "Different levels selected";
				}
			}
			return level;
		} else {
			return "No Container selected";
		}
	}

	private void updateMetricsFromContainers(List<Container> currentContainers, List<BaseMetricCalculator> metricsInformation, int possibleMetricsToRecieve) {

		GridData metricsLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		metricsLayoutData.horizontalSpan = VisualProperty.values().length;
		metricsLayoutData.heightHint = 40;
		Group metricsGroup = new Group(rootComposite, SWT.SHADOW_OUT | SWT.CENTER);
		metricsGroup.setText("Avaible Metrics");
		metricsGroup.setLayoutData(metricsLayoutData);

		DragAndDropHelper.registerAsDroppable(metricsGroup, new DropMetricOnMetricContainerListener(metricsGroup));

		int metricNumber = Math.max(1, metricsInformation.size());
		metricsGroup.setLayout(new GridLayout(metricNumber + possibleMetricsToRecieve, true));

		if (!metricsInformation.isEmpty()) {
			for (BaseMetricCalculator metricCalculator : metricsInformation) {
				DragAndDropHelper.createMetricDraggableLabel(metricsGroup, metricCalculator);
			}
		}
	}

	private List<BaseMetricCalculator> buildMetricsInformation(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			List<BaseMetricCalculator> metrics = new ArrayList<BaseMetricCalculator>();
			metrics.addAll(currentContainers.get(0).getMetrics());
			for (int i = 1; i < currentContainers.size(); i++) {
				Container container = currentContainers.get(i);
				if (!metrics.equals(container.getMetrics())) {
					return Collections.emptyList();
				}
			}
			return metrics;
		}
		return Collections.emptyList();
	}

	private void updateCurrentMappingAndVisualProperties(List<Container> containers, BiMap<BaseMetricCalculator, VisualProperty> currentMapping) {

		for (VisualProperty visualProperty : VisualProperty.values()) {
			Group visualPropGroup = new Group(rootComposite, SWT.SHADOW_OUT);
			GridData visualGroupLayoutData = new GridData(GridData.FILL_HORIZONTAL);
			visualGroupLayoutData.heightHint = 50;
			visualGroupLayoutData.minimumWidth = 80;
			visualPropGroup.setLayoutData(visualGroupLayoutData);
			visualPropGroup.setText(visualProperty.toString());
			visualPropGroup.setLayout(new GridLayout(1, true));
			BaseMetricCalculator metric = currentMapping.inverse().get(visualProperty);
			if (metric != null) {
				DragAndDropHelper.createMetricDraggableLabel(visualPropGroup, metric);
			}
			visualPropGroup.setData(VISUAL_PROPERTY, visualProperty);
			DragAndDropHelper.registerAsDroppable(visualPropGroup, new DropMetricOnVisualPropertyListener(visualPropGroup));
		}
	}

	private BiMap<BaseMetricCalculator, VisualProperty> buildCurrentMapping(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			BiMap<BaseMetricCalculator, VisualProperty> mapping = HashBiMap.create();
			mapping.putAll(currentContainers.get(0).getPropertiesMap());
			for (int i = 1; i < currentContainers.size(); i++) {
				Container container = currentContainers.get(i);
				if (!mapping.equals(container.getPropertiesMap())) {
					return HashBiMap.create();
				}
			}
			return mapping;
		}
		return HashBiMap.create();

	}

	private void updateColorScales() {
		List<IColorScale> colorScales = ColorScaleFactory.createAllColorScales();

		Group colorScalesGroup = new Group(rootComposite, SWT.SHADOW_OUT);
		GridData colorScalesLayoutData = new GridData(GridData.FILL_BOTH);
		colorScalesLayoutData.verticalSpan = 2;
		colorScalesLayoutData.minimumWidth = 100;
		colorScalesLayoutData.grabExcessHorizontalSpace = true;
		colorScalesGroup.setLayoutData(colorScalesLayoutData);
		colorScalesGroup.setLayout(new GridLayout(1, true));
		colorScalesGroup.setText("Color Scale");

		Combo combo = new Combo(colorScalesGroup, SWT.READ_ONLY);

		IColorScale currentColorScale = SeeIT3DManager.getInstance().getColorScale();
		int index = 0;
		for (IColorScale colorScale : colorScales) {
			combo.add(colorScale.getName());
			if (currentColorScale.getName().equals(colorScale.getName())) {
				combo.select(index);
			}
			index++;
		}

		Label feedbackLabel = new Label(colorScalesGroup, SWT.CENTER);
		GridData feedbackLabelData = new GridData(GridData.FILL_HORIZONTAL);
		feedbackLabel.setLayoutData(feedbackLabelData);
		feedbackLabel.setText("Min <---> Max");

		Canvas colorScaleFeedback = new Canvas(colorScalesGroup, SWT.DOUBLE_BUFFERED | SWT.BORDER);
		GridData feedbackLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		feedbackLayoutData.heightHint = 20;
		colorScaleFeedback.setLayoutData(feedbackLayoutData);

		combo.addSelectionListener(new ColorScaleSelectionListener(colorScales));

		ColorScaleDrawer drawer = new ColorScaleDrawer(currentColorScale);
		colorScaleFeedback.addPaintListener(drawer);

	}

	@Override
	public void updateMappingView(final SeeIT3DManager manager) {
		final List<Container> currentContainers = manager.getCurrentSelectedContainers();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateComponents(currentContainers);
			}

		});
	}
}
