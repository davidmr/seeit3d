package seeit3d.model.java.creator;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.metrics.ControlStructureCalculator;
import seeit3d.model.java.metrics.LOCCalculator;
import seeit3d.model.representation.LineOfCode;
import seeit3d.utils.Utils;

import com.google.common.collect.Lists;

public class MethodModelCreator extends AbstracModelCreator<IMethod, LineOfCode> {

	public MethodModelCreator(IMethod method) {
		super(method);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ControlStructureCalculator.name);
	}

	@Override
	protected LineOfCode[] fetchChildren(IMethod element) throws JavaModelException {
		return Utils.buildLinesFromMethod(element);
	}

	@Override
	protected IModelCreator lowerLevelModelCreator(LineOfCode childrenElement) {
		return null;
	}

}
