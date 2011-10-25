package seeit3d.sonar.dialog;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;

import seeit3d.sonar.analysis.SonarArtifact;

public class MetricsDialog extends Dialog {

	private final SonarArtifact artifact;

	public MetricsDialog(Shell parentShell, SonarArtifact artifact) {
		super(parentShell);
		this.artifact = artifact;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label metricLabel = new Label(composite, SWT.RIGHT);
		metricLabel.setText("Metrics");

		final List metrics = new List(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		metrics.setLayoutData(new GridData(GridData.FILL_BOTH));

		Sonar sonar = artifact.connect();
		final java.util.List<Metric> sonarMetrics = sonar.findAll(MetricQuery.all());
		for (int i = 0; i < sonarMetrics.size(); i++) {
			Metric sonarMetric = sonarMetrics.get(i);
			metrics.add(sonarMetric.getName(), i);
		}

		metrics.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int[] selectionIndices = metrics.getSelectionIndices();
				java.util.List<Metric> selectedMetrics = new ArrayList<Metric>();
				for (int i = 0; i < selectionIndices.length; i++) {
					Metric sonarMetric = sonarMetrics.get(selectionIndices[i]);
					selectedMetrics.add(sonarMetric);
				}
				artifact.useMetrics(selectedMetrics);
			}
		});

		return composite;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}

}
