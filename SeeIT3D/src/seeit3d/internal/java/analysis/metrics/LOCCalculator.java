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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.analysis.metric.AbstractNumericMetricCalculator;
import seeit3d.internal.base.error.ErrorHandler;

import com.google.inject.Singleton;

/**
 * Lines of code calculator. Only takes into account the lines of actual code, not comments
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class LOCCalculator extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = -6148807468764636601L;

	public static final LOCCalculator INSTANCE = new LOCCalculator();

	public static final String name = "LOC";

	private LOCCalculator() {
		super(name);
	}

	@Override
	public Float calculateNumericValue(Object element) {
		try {
			String source = "";
			if (element instanceof IPackageFragment) {
				IPackageFragment pack = (IPackageFragment) element;
				ICompilationUnit[] compilationUnits = pack.getCompilationUnits();
				for (ICompilationUnit iCompilationUnit : compilationUnits) {
					source += iCompilationUnit.getSource();
				}
			} else if (element instanceof ICompilationUnit) {
				ICompilationUnit cu = (ICompilationUnit) element;
				source = cu.getSource();
			} else if (element instanceof IMethod) {
				IMethod method = (IMethod) element;
				source = method.getSource();
			}
			List<String> lines = cleanLines(source.split("\n"));

			return Integer.valueOf(lines.size()).floatValue();
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return DEFAULT_VALUE;
	}

	private List<String> cleanLines(String[] lines) {

		List<String> newLines = clearSingleLineComments(lines);
		clearMultiLineComments(newLines);
		return newLines;
	}

	private List<String> clearSingleLineComments(String[] lines) {
		List<String> newLines = new ArrayList<String>();
		for (String line : lines) {
			if (!line.trim().startsWith("//")) {
				newLines.add(line);
			}
		}
		return newLines;
	}

	private void clearMultiLineComments(List<String> newLines) {
		boolean inComment = false;
		for (Iterator<String> iterator = newLines.iterator(); iterator.hasNext();) {
			String line = iterator.next().replace(" ", "");
			if (line.trim().startsWith("/*")) {
				inComment = true;
			}
			if (inComment && line.trim().endsWith("*/")) {
				iterator.remove();
				inComment = false;
			} else if (inComment) {
				iterator.remove();
			}
		}
	}

}