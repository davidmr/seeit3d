package seeit3d.model.java;

import static com.google.common.collect.Lists.*;

import java.util.List;

import org.eclipse.jdt.core.*;

import seeit3d.model.ContainerRepresentedObject;

public class JavaRepresentation implements ContainerRepresentedObject {

	private static final long serialVersionUID = 7281574875347373706L;

	private static final String LINE = "Line";

	private static final String METHOD = "Method";

	private static final String CLASS = "Class";

	private static final String PACKAGE = "Package";

	private static final String PROJECT = "Project";

	private static final List<String> granularityLevels = newArrayList(PROJECT, PACKAGE, CLASS, METHOD, LINE, "No Representation");

	private final IJavaElement javaElement;

	public JavaRepresentation(IJavaElement javaElement) {
		this.javaElement = javaElement;
	}

	@Override
	public String getName() {
		return javaElement.getElementName();
	}

	@Override
	public String granularityLevelName(int countLevelsDown) {
		int baseIndex = granularityLevels.size() - 1;
		if (javaElement instanceof IMethod) {
			baseIndex = granularityLevels.indexOf(METHOD);
		} else if (javaElement instanceof IType || javaElement instanceof ICompilationUnit) {
			baseIndex = granularityLevels.indexOf(CLASS);
		} else if (javaElement instanceof IPackageFragment) {
			baseIndex = granularityLevels.indexOf(PACKAGE);
		} else if (javaElement instanceof IJavaProject) {
			baseIndex = granularityLevels.indexOf(PROJECT);
		}
		int validatedLevel = Math.min(granularityLevels.size() - 1, baseIndex + countLevelsDown);
		return granularityLevels.get(validatedLevel);
	}

}
