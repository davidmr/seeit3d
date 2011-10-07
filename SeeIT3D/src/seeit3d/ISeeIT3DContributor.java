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
package seeit3d;

import java.util.Map;

import seeit3d.analysis.IModelDataProvider;

/**
 * Interface that defines an external module that contributes to SeeIT 3D. Each of the implementations must be registered using the SeeIT3D.contribute method
 * 
 * @author David Montaño
 * 
 */
public interface ISeeIT3DContributor {
	
	void initialize();
	
	Map<String, Class<? extends IModelDataProvider>> configureDataProviders();

}
