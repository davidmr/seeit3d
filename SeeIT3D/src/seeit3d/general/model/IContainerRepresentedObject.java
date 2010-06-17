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
package seeit3d.general.model;

import java.io.Serializable;

/**
 * Definition of what a container represents. This means the element that the container in the view is showing/representing. It must have a name and must be able to tell what is the name of the
 * granularity levels down of it.
 * 
 * @author David Montaño
 * 
 */
public interface IContainerRepresentedObject extends Serializable {

	public String getName();

	public String granularityLevelName(int countLevelsDown);

}
