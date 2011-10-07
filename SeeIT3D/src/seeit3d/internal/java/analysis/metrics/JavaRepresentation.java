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
package seeit3d.internal.java.analysis.metrics;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import seeit3d.internal.base.model.IContainerRepresentedObject;

/**
 * This class represents a <code>IJavaElement</code> associated to a container in the view
 * 
 * @author David Montaño
 * 
 */
public class JavaRepresentation implements IContainerRepresentedObject {

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

	@Override
	public String toString() {
		return getName();
	}

}
