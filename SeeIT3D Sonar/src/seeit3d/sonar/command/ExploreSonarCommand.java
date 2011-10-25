package seeit3d.sonar.command;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import seeit3d.jobs.VisualizeJob;
import seeit3d.sonar.SonarConstants;
import seeit3d.sonar.analysis.SonarArtifact;
import seeit3d.sonar.dialog.MetricsDialog;
import seeit3d.sonar.dialog.SonarConnectionDialog;

public class ExploreSonarCommand extends AbstractHandler {

	private ServiceTracker<IProxyService, IProxyService> proxyTracker;

	public ExploreSonarCommand() {
		Class<? extends ExploreSonarCommand> clazz = this.getClass();
		BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
		proxyTracker = new ServiceTracker<IProxyService, IProxyService>(bundleContext, IProxyService.class, null);
		proxyTracker.open();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Shell shell = HandlerUtil.getActiveShell(event);
			SonarConnectionDialog dialog = new SonarConnectionDialog(shell);
			dialog.setBlockOnOpen(true);
			int code = dialog.open();

			if (code == Dialog.OK) {
				SonarArtifact artifact = dialog.getSonarArtifact();
				IProxyService proxyService = proxyTracker.getService();
				IProxyData[] proxyDatas = proxyService.select(new URI(artifact.getHost()));
				for (IProxyData data : proxyDatas) {
					if (data.getHost() != null) {
						System.setProperty("http.proxySet", "true");
						System.setProperty("http.proxyHost", data.getHost());
					}
					if (data.getHost() != null) {
						System.setProperty("http.proxyPort", String.valueOf(data.getPort()));
					}
					if (data.getUserId() != null) {
						System.setProperty("http.proxyUser", data.getUserId());
					}
					if (data.getPassword() != null) {
						System.setProperty("http.proxyPassword", data.getPassword());
					}
				}
				proxyService = null;
				MetricsDialog metricsDialog = new MetricsDialog(shell, artifact);
				metricsDialog.setBlockOnOpen(true);
				code = metricsDialog.open();
				if (code == Dialog.OK) {
					List<SonarArtifact> artifacts = new ArrayList<SonarArtifact>();
					artifacts.add(artifact);
					VisualizeJob job = new VisualizeJob(shell, SonarConstants.MODEL_GENERATOR_KEY, artifacts);
					job.schedule();
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	}

}
