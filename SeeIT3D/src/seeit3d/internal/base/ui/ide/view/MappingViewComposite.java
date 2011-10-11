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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.MappingViewNeedsUpdateEvent;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.VisualProperty;
import seeit3d.internal.base.ui.ide.view.dnd.DragAndDropHelper;
import seeit3d.internal.base.ui.ide.view.dnd.DropMetricOnMetricContainerListener;
import seeit3d.internal.base.ui.ide.view.dnd.DropMetricOnVisualPropertyListener;
import seeit3d.internal.base.ui.ide.view.listeners.ChangeLevelOption;
import seeit3d.internal.base.ui.ide.view.listeners.ColorScaleDrawer;
import seeit3d.internal.base.ui.ide.view.listeners.ColorScaleSelectionListener;
import seeit3d.internal.base.ui.ide.view.listeners.RelationshipSelectionListener;
import seeit3d.internal.base.ui.ide.view.listeners.SelectionComponentListener;
import seeit3d.internal.base.visual.api.IColorScaleRegistry;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;
import seeit3d.internal.base.visual.api.ISeeIT3DVisualProperties;
import seeit3d.internal.base.visual.colorscale.IColorScale;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;
import seeit3d.internal.base.visual.relationships.imp.NoRelationships;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;

/**
 * This class represents the mapping view.
 * 
 * @author David Montaño
 * 
 */
public class MappingViewComposite extends Composite implements IEventListener {

	private static final int LAST_COLUMN_WIDTH = 160;

	private static final int FIRST_COLUMN_WIDTH = 200;

	private static final int MIN_HEIGHT = 60;

	public static final String VISUAL_PROPERTY = "visualProperty";

	private ISeeIT3DVisualProperties seeIT3DVisualProperties;

	private IColorScaleRegistry colorScaleRegistry;

	private IRelationshipsRegistry relationshipsRegistry;

	private Composite rootComposite;

	private ScrolledComposite scrollerComposite;

	public MappingViewComposite(Composite parent) {
		super(parent, SWT.DOUBLE_BUFFERED);
		SeeIT3D.injector().injectMembers(this);
		this.setLayout(new GridLayout(1, true));
		updateComponents(new ArrayList<Container>());
	}

	private void updateComponents(List<Container> containers) {
		cleanComposites();

		scrollerComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
		scrollerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrollerComposite.setExpandHorizontal(true);
		scrollerComposite.setMinWidth(800);

		rootComposite = new Composite(scrollerComposite, SWT.DOUBLE_BUFFERED);
		scrollerComposite.setContent(rootComposite);

		rootComposite.setLayout(new GridLayout(3, false));

		updateCurrentGranularityLevelFromContainers(containers);

		List<MetricCalculator> metricsInformation = buildMetricsInformation(containers);
		BiMap<MetricCalculator, VisualProperty> currentMapping = buildCurrentMapping(containers);

		removeAlreadyMappedMetrics(metricsInformation, currentMapping);
		updateCurrentMappingAndVisualProperties(containers, currentMapping);
		updateColorScales();
		updateRelationshipsGenerator(containers);
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

	private void removeAlreadyMappedMetrics(List<MetricCalculator> metricsInformation, BiMap<MetricCalculator, VisualProperty> currentMapping) {
		for (Iterator<MetricCalculator> iterator = metricsInformation.iterator(); iterator.hasNext();) {
			MetricCalculator metric = iterator.next();
			if (currentMapping.containsKey(metric)) {
				iterator.remove();
			}
		}
	}

	private void updateCurrentGranularityLevelFromContainers(List<Container> currentContainers) {

		String currentGranularityLevelFromContainers = getCurrentGranularityLevelFromContainers(currentContainers);
		SelectionComponentListener selectionComponentListener = new SelectionComponentListener();
		Group componentSelectionGroup = new Group(rootComposite, SWT.SHADOW_OUT);
		GridData componetLayoutData = new GridData(GridData.FILL_VERTICAL);
		componetLayoutData.widthHint = FIRST_COLUMN_WIDTH;
		componetLayoutData.heightHint = MIN_HEIGHT;

		componentSelectionGroup.setLayoutData(componetLayoutData);
		componentSelectionGroup.setText("Granularity Level");
		componentSelectionGroup.setLayout(new GridLayout(3, false));

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

	private void updateMetricsFromContainers(List<Container> currentContainers, List<MetricCalculator> metricsInformation, int possibleMetricsToRecieve) {

		GridData metricsLayoutData = new GridData(GridData.FILL_BOTH);
		metricsLayoutData.heightHint = MIN_HEIGHT;

		Group metricsGroup = new Group(rootComposite, SWT.SHADOW_OUT | SWT.CENTER);
		metricsGroup.setText("Available Metrics");
		metricsGroup.setLayoutData(metricsLayoutData);

		DragAndDropHelper.registerAsDroppable(metricsGroup, new DropMetricOnMetricContainerListener(metricsGroup));

		metricsGroup.setLayout(new GridLayout(4, false));

		if (!metricsInformation.isEmpty()) {
			for (MetricCalculator metricCalculator : metricsInformation) {
				DragAndDropHelper.createMetricDraggableLabel(metricsGroup, metricCalculator);
			}
		}
	}

	private List<MetricCalculator> buildMetricsInformation(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
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

	private void updateCurrentMappingAndVisualProperties(List<Container> containers, BiMap<MetricCalculator, VisualProperty> currentMapping) {
		Composite visualPropertiesComposite = new Composite(rootComposite, SWT.DOUBLE_BUFFERED);
		visualPropertiesComposite.setLayout(new GridLayout(VisualProperty.values().length, true));
		GridData visualPropertiesLayoutData = new GridData(GridData.FILL_BOTH);
		visualPropertiesLayoutData.minimumWidth = 150;
		visualPropertiesComposite.setLayoutData(visualPropertiesLayoutData);

		for (VisualProperty visualProperty : VisualProperty.values()) {
			Group visualPropGroup = new Group(visualPropertiesComposite, SWT.SHADOW_OUT);
			GridData visualGroupLayoutData = new GridData(GridData.FILL_BOTH);
			visualGroupLayoutData.minimumHeight = MIN_HEIGHT;

			visualPropGroup.setLayoutData(visualGroupLayoutData);
			visualPropGroup.setText(visualProperty.toString());
			visualPropGroup.setLayout(new GridLayout(1, true));
			MetricCalculator metric = currentMapping.inverse().get(visualProperty);
			if (metric != null) {
				DragAndDropHelper.createMetricDraggableLabel(visualPropGroup, metric);
			}
			visualPropGroup.setData(VISUAL_PROPERTY, visualProperty);
			DragAndDropHelper.registerAsDroppable(visualPropGroup, new DropMetricOnVisualPropertyListener(visualPropGroup));
		}
	}

	private BiMap<MetricCalculator, VisualProperty> buildCurrentMapping(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			BiMap<MetricCalculator, VisualProperty> mapping = HashBiMap.create();
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
		Iterable<IColorScale> allColorScales = colorScaleRegistry.allColorScales();

		Group colorScalesGroup = new Group(rootComposite, SWT.SHADOW_OUT);
		GridData colorScalesLayoutData = new GridData(GridData.FILL_VERTICAL);
		colorScalesLayoutData.verticalSpan = 2;
		colorScalesLayoutData.widthHint = LAST_COLUMN_WIDTH;

		colorScalesGroup.setLayoutData(colorScalesLayoutData);
		colorScalesGroup.setLayout(new GridLayout(1, true));
		colorScalesGroup.setText("Color Scale");

		Combo combo = new Combo(colorScalesGroup, SWT.READ_ONLY);

		IColorScale currentColorScale = seeIT3DVisualProperties.getCurrentColorScale();
		int index = 0;
		for (IColorScale colorScale : allColorScales) {
			combo.add(colorScale.getName());
			if (currentColorScale.getName().equals(colorScale.getName())) {
				combo.select(index);
			}
			index++;
		}

		Label feedbackLabel = new Label(colorScalesGroup, SWT.CENTER);
		GridData feedbackLabelData = new GridData(GridData.FILL_HORIZONTAL);
		feedbackLabel.setLayoutData(feedbackLabelData);
		feedbackLabel.setText("Min <-------> Max");

		Canvas colorScaleFeedback = new Canvas(colorScalesGroup, SWT.DOUBLE_BUFFERED | SWT.BORDER);

		GridData feedbackLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		feedbackLayoutData.heightHint = 20;
		colorScaleFeedback.setLayoutData(feedbackLayoutData);

		combo.addSelectionListener(new ColorScaleSelectionListener());

		ColorScaleDrawer drawer = new ColorScaleDrawer(currentColorScale);
		colorScaleFeedback.addPaintListener(drawer);

	}

	private void updateRelationshipsGenerator(List<Container> containers) {
		Iterable<Class<? extends ISceneGraphRelationshipGenerator>> allRelationshipsGenerator = relationshipsRegistry.allRelationshipsGenerator();

		Group relationshipsGroup = new Group(rootComposite, SWT.SHADOW_OUT);
		GridData relationshipsLayoutData = new GridData(GridData.FILL_VERTICAL);
		relationshipsLayoutData.widthHint = FIRST_COLUMN_WIDTH;
		relationshipsLayoutData.heightHint = MIN_HEIGHT;
		relationshipsGroup.setLayoutData(relationshipsLayoutData);
		relationshipsGroup.setLayout(new GridLayout(1, true));
		relationshipsGroup.setText("Relationship visual type");

		Combo combo = new Combo(relationshipsGroup, SWT.READ_ONLY);

		Class<? extends ISceneGraphRelationshipGenerator> selectedGenerator = buildCurrentRelationShipGenerator(containers);
		int index = 0;

		String selectedRelation = relationshipsRegistry.getRelationName(selectedGenerator);
		for (Class<? extends ISceneGraphRelationshipGenerator> generatorClass : allRelationshipsGenerator) {
			String relationToAdd = relationshipsRegistry.getRelationName(generatorClass);
			combo.add(relationToAdd);
			if (relationToAdd.equals(selectedRelation)) {
				combo.select(index);
			}
			index++;
		}
		combo.addSelectionListener(new RelationshipSelectionListener());

	}

	private Class<? extends ISceneGraphRelationshipGenerator> buildCurrentRelationShipGenerator(List<Container> currentContainers) {
		if (!currentContainers.isEmpty()) {
			Class<? extends ISceneGraphRelationshipGenerator> relationClazz = currentContainers.get(0).getSceneGraphRelationshipGenerator().getClass();
			for (int i = 1; i < currentContainers.size(); i++) {
				Container container = currentContainers.get(i);
				if (!relationClazz.equals(container.getSceneGraphRelationshipGenerator().getClass())) {
					return NoRelationships.class;
				}
			}
			return relationClazz;
		}
		return NoRelationships.class;
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof MappingViewNeedsUpdateEvent) {
			final List<Container> currentContainers = ((MappingViewNeedsUpdateEvent) event).getContainers();
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					updateComponents(currentContainers);
				}
			});
		}
	}

	@Inject
	public void setSeeIT3DVisualProperties(ISeeIT3DVisualProperties seeIT3DVisualProperties) {
		this.seeIT3DVisualProperties = seeIT3DVisualProperties;
	}

	@Inject
	public void setColorScaleRegistry(IColorScaleRegistry colorScaleRegistry) {
		this.colorScaleRegistry = colorScaleRegistry;
	}

	@Inject
	public void setRelationshipsRegistry(IRelationshipsRegistry relationshipsRegistry) {
		this.relationshipsRegistry = relationshipsRegistry;
	}
}
