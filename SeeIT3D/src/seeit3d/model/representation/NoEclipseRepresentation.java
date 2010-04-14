package seeit3d.model.representation;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

import seeit3d.model.EclipseResourceRepresentation;

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
