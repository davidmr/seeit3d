package seeit3d.sonar.analysis;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient3Connector;
import org.sonar.wsclient.services.Metric;


public class SonarArtifact {

	private final String host;

	private final String user;

	private final String password;

	private final String artifactId;

	private List<Metric> metrics;

	public SonarArtifact(String host, String user, String password, String artifactId) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.artifactId = artifactId;
	}

	public String getHost() {
		return host;
	}

	public boolean requiresAuthentication() {
		return user != null && password != null;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void useMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	public SonarArtifact cloneForArtifact(String otherArtifact) {
		SonarArtifact clone = new SonarArtifact(host, user, password, otherArtifact);
		clone.metrics = metrics;
		return clone;
	}

	public Sonar connect() {
		Host host = new Host(this.host);
		if (requiresAuthentication()) {
			host.setUsername(user);
			host.setPassword(password);
		}

		if (System.getProperty("http.proxySet") != null && System.getProperty("http.proxySet").equals("true")) {
			String proxyHost = System.getProperty("http.proxyHost");
			Integer proxyPort = Integer.valueOf(System.getProperty("http.proxyPort"));
			String user = System.getProperty("http.proxyUser");
			String password = System.getProperty("http.proxyPassword");
			org.apache.commons.httpclient.HttpClient client = new HttpClient();
			client.getHostConfiguration().setProxy(proxyHost, proxyPort);
			if (user != null && password != null) {
				client.getState().setProxyCredentials(new AuthScope(proxyHost, proxyPort), new UsernamePasswordCredentials(user, password));
			}

			return new Sonar(new HttpClient3Connector(host, client));
		} else {
			return new Sonar(new HttpClient3Connector(host));
		}

	}

	@Override
	public String toString() {
		return artifactId;
	}


}
