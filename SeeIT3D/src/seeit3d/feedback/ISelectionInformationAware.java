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
package seeit3d.feedback;

import java.util.Map;

import seeit3d.core.model.Container;

/**
 * Defines an object which listens to changes on selected information in the visualization area.
 * 
 * @author David Montaño
 * 
 */
public interface ISelectionInformationAware {
	
	void updateInformation(Iterable<Container> selectedContainers, Map<String, String> metricValues);
	
}
