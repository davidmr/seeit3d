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

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

/**
 * Indicates the representation associated with a polycylinder of a resource within the Eclipse IDE. It is used to navigate to the associated elements from the visualization.
 * 
 * @author David Montaño
 * 
 */
public interface IEclipseResourceRepresentation extends Serializable {

	public IResource getAssociatedResource();

	public boolean hasJavaElementRepresentation();

	public IJavaElement getAssociatedJavaElement();

}
