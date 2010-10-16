package seeit3d.java;

import seeit3d.base.ISeeIT3DContributor;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class JavaContribution implements ISeeIT3DContributor {

	public static final String JAVA_FILE = "JAVA_FILE";

	public static final String PACKAGE = "PACKAGE";

	public static final String PROJECT = "PROJECT";

	private static Injector injector;

	@Override
	public void initialize() {
		try {
			injector = Guice.createInjector(new JavaModule());
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
			throw new IllegalArgumentException("Java is not initialized");
		}
	}

}
