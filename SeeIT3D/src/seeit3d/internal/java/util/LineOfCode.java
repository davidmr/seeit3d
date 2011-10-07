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
package seeit3d.internal.java.util;

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

	private IMethod method;

	public LineOfCode(int numberOfLine, String line, IMethod method) {
		this.line = line;
		this.method = method;
		this.numberOfLine = numberOfLine;
	}

	public IMethod getMethod() {
		return method;
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
		return String.valueOf(numberOfLine);
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
		return method.getJavaProject();
	}

	@Override
	public IOpenable getOpenable() {
		return null;
	}

	@Override
	public IJavaElement getParent() {
		return method;
	}

	@Override
	public IPath getPath() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IJavaElement getPrimaryElement() {
		return method.getPrimaryElement();
	}

	@Override
	public IResource getResource() {
		return method.getResource();
	}

	@Override
	public ISchedulingRule getSchedulingRule() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public IResource getUnderlyingResource() throws JavaModelException {
		return method.getUnderlyingResource();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean isStructureKnown() throws JavaModelException {
		return method.isStructureKnown();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getLine() {
		return line;
	}

}
