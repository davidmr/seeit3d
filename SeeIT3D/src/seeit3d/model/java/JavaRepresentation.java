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

	private final String elementName;

	private final String granularityLevel;

	public JavaRepresentation(IJavaElement javaElement) {
		elementName = javaElement.getElementName();
		granularityLevel = selectGranularityLevel(javaElement);
	}

	private String selectGranularityLevel(IJavaElement javaElement) {
		if (javaElement instanceof IMethod) {
			return METHOD;
		} else if (javaElement instanceof IType || javaElement instanceof ICompilationUnit) {
			return CLASS;
		} else if (javaElement instanceof IPackageFragment) {
			return PACKAGE;
		} else if (javaElement instanceof IJavaProject) {
			return PROJECT;
		} else {
			return "Undefined";
		}
	}

	@Override
	public String getName() {
		return elementName;
	}

	@Override
	public String granularityLevelName(int countLevelsDown) {
		int baseIndex = granularityLevels.indexOf(granularityLevel);
		int validatedLevel = Math.min(granularityLevels.size() - 1, baseIndex + countLevelsDown);
		return granularityLevels.get(validatedLevel);
	}

}
