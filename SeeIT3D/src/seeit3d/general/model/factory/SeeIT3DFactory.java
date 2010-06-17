/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.general.model.factory;

import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

/**
 * Factory to instantiate the classes enabled to do it by using this factory.
 * 
 * @author David Montaño
 * 
 */
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
