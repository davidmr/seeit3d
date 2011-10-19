package seeit3d.sonar.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import seeit3d.sonar.analysis.SonarArtifact;

public class SonarConnectionDialog extends Dialog {

	private Text host;

	private Text artifactId;

	private Text user;

	private Text password;

	private SonarArtifact sonarArtifact;

	public SonarConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		GridData fill = new GridData(GridData.FILL_HORIZONTAL);
		GridData label = new GridData();
		label.widthHint = 70;

		Label hostLabel = new Label(composite, SWT.RIGHT);
		hostLabel.setText("Host");
		hostLabel.setLayoutData(label);

		host = new Text(composite, SWT.SINGLE);
		host.setLayoutData(fill);

		Label artifactLabel = new Label(composite, SWT.RIGHT);
		artifactLabel.setText("Artifact ID");
		artifactLabel.setLayoutData(label);

		artifactId = new Text(composite, SWT.SINGLE);
		artifactId.setLayoutData(fill);

		Label userLabel = new Label(composite, SWT.RIGHT);
		userLabel.setText("User");
		userLabel.setLayoutData(label);

		user = new Text(composite, SWT.SINGLE);
		user.setLayoutData(fill);

		Label passwordLabel = new Label(composite, SWT.RIGHT);
		passwordLabel.setText("Password");
		passwordLabel.setLayoutData(label);

		password = new Text(composite, SWT.SINGLE | SWT.PASSWORD);
		password.setLayoutData(fill);

		return composite;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 200);
	}

	@Override
	protected void okPressed() {
		sonarArtifact = new SonarArtifact(host.getText(), user.getText(), password.getText(), artifactId.getText());
		super.okPressed();
	}

	public SonarArtifact getSonarArtifact() {
		return sonarArtifact;
	}

}
