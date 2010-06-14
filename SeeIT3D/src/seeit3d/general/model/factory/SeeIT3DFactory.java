package seeit3d.general.model.factory;

import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

public class SeeIT3DFactory {

	private static final ClassToInstanceMap<Object> instances;

	static {
		instances = MutableClassToInstanceMap.create();
	}

	public synchronized static <T> T getInstance(Class<T> clazz) {
		SeeIT3DFactoryEnabled annotation = clazz.getAnnotation(SeeIT3DFactoryEnabled.class);
		if (annotation == null) {
			throw new IllegalArgumentException(clazz.getCanonicalName() + " is not annotated as " + SeeIT3DFactoryEnabled.class.getCanonicalName());
		} else {
			T instance = null;
			boolean singleton = annotation.singleton();
			if (singleton) {
				instance = instances.getInstance(clazz);
				if (instance == null) {
					instance = instantiate(clazz);
					instances.put(clazz, instance);
				}
			} else {
				instance = instantiate(clazz);
			}
			return instance;
		}
	}

	private static <T> T instantiate(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
