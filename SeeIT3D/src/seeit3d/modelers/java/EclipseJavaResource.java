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
package seeit3d.modelers.java;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.IEclipseResourceRepresentation;


/**
 * Represents a IJavaElement which can be associated as an Eclipse Resource to a polycylinder
 * 
 * @author David Montaño
 * 
 */
public class EclipseJavaResource implements IEclipseResourceRepresentation {

	private static final long serialVersionUID = -253239942112314950L;

	private final IJavaElement javaElement;

	public EclipseJavaResource(IJavaElement javaElement) {
		this.javaElement = javaElement;
	}

	@Override
	public IResource getAssociatedResource() {
		try {
			return javaElement.getUnderlyingResource();
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
			return null;
		}
	}

	@Override
	public boolean hasJavaElementRepresentation() {
		return true;
	}

	@Override
	public IJavaElement getAssociatedJavaElement() {
		return javaElement;
	}

}
