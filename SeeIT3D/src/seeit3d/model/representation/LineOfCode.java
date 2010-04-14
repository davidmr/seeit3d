package seeit3d.model.representation;

import java.io.Serializable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.*;

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
