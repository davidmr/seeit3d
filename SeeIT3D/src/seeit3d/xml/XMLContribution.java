package seeit3d.xml;

import seeit3d.base.ISeeIT3DContributor;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class XMLContribution implements ISeeIT3DContributor {

	private static Injector injector;

	@Override
	public void initialize() {
		try {
			injector = Guice.createInjector(new XMLModule());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Injector injector() {
		checkInitialized();
		return injector;
	}

	private static void checkInitialized() {
		if (injector == null) {
			throw new IllegalArgumentException("XML is not initialized");
		}
	}

}
