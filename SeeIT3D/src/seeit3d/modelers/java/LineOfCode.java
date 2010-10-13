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

import java.io.Serializable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Represents a line of code which can be handled as a IJavaElement
 * 
 * @author David Montaño
 * 
 */
public class LineOfCode implements IJavaElement, Serializable {

	private static final long serialVersionUID = 5279009769282903506L;

	private int numberOfLine;

	private String line;

	private IMethod parent;

	public LineOfCode(int numberOfLine, String line, IMethod parent) {
		this.line = line;
		this.parent = parent;
		this.numberOfLine = numberOfLine;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public IJavaElement getAncestor(int ancestorType) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getAttachedJavadoc(IProgressMonitor monitor) throws JavaModelException {
		return null;
	}

	@Override
	public IResource getCorrespondingResource() throws JavaModelException {
		return null;
	}

	@Override
	public String getElementName() {
		StringBuilder sb = new StringBuilder();
		sb.append("Line [");
		sb.append(numberOfLine);
		sb.append("] ");
		sb.append(" in Method ");
		sb.append(parent.getElementName());
		return sb.toString();
	}

	@Override
	public int getElementType() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getHandleIdentifier() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IJavaModel getJavaModel() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IJavaProject getJavaProject() {
		return parent.getJavaProject();
	}

	@Override
	public IOpenable getOpenable() {
		return null;
	}

	@Override
	public IJavaElement getParent() {
		return parent;
	}

	@Override
	public IPath getPath() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IJavaElement getPrimaryElement() {
		return parent.getPrimaryElement();
	}

	@Override
	public IResource getResource() {
		return parent.getResource();
	}

	@Override
	public ISchedulingRule getSchedulingRule() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IResource getUnderlyingResource() throws JavaModelException {
		return parent.getUnderlyingResource();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean isStructureKnown() throws JavaModelException {
		return parent.isStructureKnown();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getLine() {
		return line;
	}

}
