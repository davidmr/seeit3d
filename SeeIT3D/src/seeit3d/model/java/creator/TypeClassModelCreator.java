package seeit3d.model.java.creator;

import java.util.List;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.metrics.ComplexityCalculator;
import seeit3d.model.java.metrics.LOCCalculator;

import com.google.common.collect.Lists;

public class TypeClassModelCreator extends AbstracModelCreator<IType, IMethod> {

	public TypeClassModelCreator(IType elementToAnalize) {
		super(elementToAnalize);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ComplexityCalculator.name);
	}

	@Override
	protected IMethod[] fetchChildren(IType element) throws JavaModelException {
		return element.getMethods();
	}

	@Override
	protected IModelCreator lowerLevelModelCreator(IMethod childrenElement) {
		return new MethodModelCreator(childrenElement);
	}

}