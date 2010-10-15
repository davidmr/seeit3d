package seeit3d.general;

import seeit3d.core.SeeIT3DCoreModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SeeIT3D {

	private static Injector injector;

	public synchronized static void initialize() {
		injector = Guice.createInjector(new SeeIT3DCoreModule());
	}

	public static Injector injector() {
		checkInitialized();
		return injector;
	}

	private static void checkInitialized() {
		if (injector == null) {
			throw new IllegalStateException("SeeIT 3D not initialized");
		}
	}
}
