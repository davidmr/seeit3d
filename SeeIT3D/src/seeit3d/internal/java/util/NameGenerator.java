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

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

/**
 * Class to generate names for <code>IJavaElements</code>
 * 
 * @author David Montaño
 * 
 */
public class NameGenerator {

	public static String generateNameFor(IJavaProject project) {
		return project.getElementName();
	}

	public static String generateNameFor(IPackageFragment fragment) {
		return fragment.getElementName();
	}

	public static String generateNameFor(IType type) {
		return generateNameFor(type.getPackageFragment()) + "." + type.getElementName();
	}

	public static String generateNameFor(ICompilationUnit compilation) {
		IPackageFragment fragment = (IPackageFragment) compilation.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
		return generateNameFor(fragment) + "." + compilation.getElementName();
	}

	public static String generateNameFor(IMethod method) {
		return generateNameFor(method.getDeclaringType()) + "." + method.getElementName();
	}

	public static String generateNameFor(LineOfCode line) {
		return generateNameFor(line.getMethod()) + " - " + line.getElementName();
	}

}
