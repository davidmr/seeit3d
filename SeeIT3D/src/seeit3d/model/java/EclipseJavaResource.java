package seeit3d.model.java;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.error.ErrorHandler;
import seeit3d.model.EclipseResourceRepresentation;

public class EclipseJavaResource implements EclipseResourceRepresentation {

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
