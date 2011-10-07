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
package seeit3d.internal.base.visual.api;

import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;

/**
 * Interface that defines the operations performed by the relationships registry. It works on base of the class object of each implementation
 * 
 * @author David Montaño
 * 
 */
public interface IRelationshipsRegistry {

	void registerRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generator);

	Iterable<Class<? extends ISceneGraphRelationshipGenerator>> allRelationshipsGenerator();

	String getRelationName(Class<? extends ISceneGraphRelationshipGenerator> selectedGenerator);

	ISceneGraphRelationshipGenerator createNewInstance(Class<? extends ISceneGraphRelationshipGenerator> generator);

}
