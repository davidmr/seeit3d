package seeit3d.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.*;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.generator.*;
import seeit3d.java.modeler.generator.annotation.*;
import seeit3d.java.modeler.generator.metrics.*;
import seeit3d.java.modeler.generator.metrics.annotation.*;

import com.google.inject.*;
import com.google.inject.name.Names;

public class JavaModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(new TypeLiteral<IModelGenerator<IMethod>>() {}).annotatedWith(MethodModeler.class).to(MethodModelGenerator.class);
		bind(new TypeLiteral<IModelGenerator<ICompilationUnit>>() {}).annotatedWith(CompilationUnitModeler.class).to(CompilationUnitModelGenerator.class);
		bind(new TypeLiteral<IModelGenerator<IPackageFragment>>() {}).annotatedWith(PackageModeler.class).to(PackageModelGenerator.class);
		bind(new TypeLiteral<IModelGenerator<IJavaProject>>() {}).annotatedWith(ProjectModeler.class).to(ProjectModelGenerator.class);
		bind(new TypeLiteral<IModelGenerator<IType>>() {}).annotatedWith(TypeModeler.class).to(TypeClassModelGenerator.class);

		Map<String, Provider<? extends IModelGenerator<? extends IJavaElement>>> modelMap = new HashMap<String, Provider<? extends IModelGenerator<? extends IJavaElement>>>();
		modelMap.put(JavaContribution.JAVA_FILE, getProvider(CompilationUnitModelGenerator.class));
		modelMap.put(JavaContribution.PACKAGE, getProvider(PackageModelGenerator.class));
		modelMap.put(JavaContribution.PROJECT, getProvider(ProjectModelGenerator.class));

		bind(new TypeLiteral<Map<String, Provider<? extends IModelGenerator<? extends IJavaElement>>>>() {}).annotatedWith(Names.named("modelMap")).toInstance(modelMap);

		bind(MetricCalculator.class).annotatedWith(LOC.class).to(LOCCalculator.class);
		bind(MetricCalculator.class).annotatedWith(LCOM.class).to(LCOMCalculator.class);
		bind(MetricCalculator.class).annotatedWith(McCabeComplexity.class).to(McCabeComplexityCalculator.class);
		bind(MetricCalculator.class).annotatedWith(ControlStructure.class).to(ControlStructureCalculator.class);

	}

}
