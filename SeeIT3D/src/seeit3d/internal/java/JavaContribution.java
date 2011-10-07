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
package seeit3d.internal.java;

import java.util.HashMap;
import java.util.Map;

import seeit3d.ISeeIT3DContributor;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.internal.java.analysis.MethodDataProvider;
import seeit3d.internal.java.analysis.PackageDataProvider;
import seeit3d.internal.java.analysis.ProjectDataProvider;
import seeit3d.internal.java.analysis.TypeDataProvider;

/**
 * The Java contribution to SeeIT 3D
 * 
 * @author David Montaño
 * 
 */
public class JavaContribution implements ISeeIT3DContributor {

	public static final String JAVA_FILE = "JAVA_FILE";

	public static final String PACKAGE = "PACKAGE";

	public static final String PROJECT = "PROJECT";

	public static final String MODEL_PROVIDER_KEY_PROJECT = "seeit3d.local.java.project";

	public static final String MODEL_PROVIDER_KEY_PACKAGE = "seeit3d.local.java.package";

	public static final String MODEL_PROVIDER_KEY_TYPE = "seeit3d.local.java.type";

	public static final String MODEL_PROVIDER_KEY_METHOD = "seeit3d.local.java.method";

	@Override
	public void initialize() {}

	@Override
	public Map<String, Class<? extends IModelDataProvider>> configureDataProviders() {
		Map<String, Class<? extends IModelDataProvider>> map = new HashMap<String, Class<? extends IModelDataProvider>>();
		map.put(MODEL_PROVIDER_KEY_PROJECT, ProjectDataProvider.class);
		map.put(MODEL_PROVIDER_KEY_PACKAGE, PackageDataProvider.class);
		map.put(MODEL_PROVIDER_KEY_TYPE, TypeDataProvider.class);
		map.put(MODEL_PROVIDER_KEY_METHOD, MethodDataProvider.class);
		return map;
	}

}
