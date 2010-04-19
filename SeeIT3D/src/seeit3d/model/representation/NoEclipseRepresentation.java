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

package seeit3d.model.representation;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

import seeit3d.model.EclipseResourceRepresentation;

/**
 * Represents an object which has no Eclipse resource representation as in the case of XML read elements
 * 
 * @author David Montaño
 * 
 */
public class NoEclipseRepresentation implements EclipseResourceRepresentation {

	private static final long serialVersionUID = 5497173695068543531L;

	@Override
	public IJavaElement getAssociatedJavaElement() {
		return null;
	}

	@Override
	public IResource getAssociatedResource() {
		return null;
	}

	@Override
	public boolean hasJavaElementRepresentation() {
		return false;
	}

}
