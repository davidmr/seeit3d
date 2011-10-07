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
package seeit3d.internal.xml;

import java.util.HashMap;
import java.util.Map;

import seeit3d.ISeeIT3DContributor;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.internal.xml.analysis.XMLDataProvider;

/**
 * The XML contribution to SeeIT 3D
 * 
 * @author David Montaño
 * 
 */
public class XMLContribution implements ISeeIT3DContributor {

	public static final String MODEL_PROVIDER_KEY_XML = "seeit3d.local.xml";

	@Override
	public void initialize() {}

	@Override
	public Map<String, Class<? extends IModelDataProvider>> configureDataProviders() {
		Map<String, Class<? extends IModelDataProvider>> map = new HashMap<String, Class<? extends IModelDataProvider>>();
		map.put(MODEL_PROVIDER_KEY_XML, XMLDataProvider.class);
		return map;
	}

}
