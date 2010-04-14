package seeit3d.model;

import java.io.Serializable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;

public interface EclipseResourceRepresentation extends Serializable {

	public IResource getAssociatedResource();

	public boolean hasJavaElementRepresentation();

	public IJavaElement getAssociatedJavaElement();

}
