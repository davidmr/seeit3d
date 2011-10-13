package seeit3d.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import seeit3d.analysis.IModelDataProvider;
import seeit3d.internal.base.analysis.PluginProxyModelDataProvider;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.java.JavaConstants;
import seeit3d.internal.java.analysis.MethodDataProvider;
import seeit3d.internal.java.analysis.PackageDataProvider;
import seeit3d.internal.java.analysis.ProjectDataProvider;
import seeit3d.internal.java.analysis.TypeDataProvider;
import seeit3d.internal.xml.XMLConstants;
import seeit3d.internal.xml.analysis.XMLDataProvider;

public class ModelDataProviderRegistry {

	private static final String SEEIT3D_EXTENSIONPOINT = "seeit3d.extensionpoint";

	private static Map<String, IModelDataProvider> modelGenerators;

	public synchronized static IModelDataProvider getModelGenerator(String modelGeneratorKey) {
		initializeIfNecessary();

		IModelDataProvider modelGenerator = modelGenerators.get(modelGeneratorKey);
		if (modelGenerator == null) {
			throw new SeeIT3DException("No model generator register for " + modelGeneratorKey);
		}
		return modelGenerator;
	}

	private static void initializeIfNecessary() {
		if (modelGenerators == null) {
			modelGenerators = new HashMap<String, IModelDataProvider>();
			contributeInternalModelDataProvider(JavaConstants.MODEL_PROVIDER_KEY_PROJECT, ProjectDataProvider.class);
			contributeInternalModelDataProvider(JavaConstants.MODEL_PROVIDER_KEY_PACKAGE, PackageDataProvider.class);
			contributeInternalModelDataProvider(JavaConstants.MODEL_PROVIDER_KEY_TYPE, TypeDataProvider.class);
			contributeInternalModelDataProvider(JavaConstants.MODEL_PROVIDER_KEY_METHOD, MethodDataProvider.class);

			contributeInternalModelDataProvider(XMLConstants.MODEL_PROVIDER_KEY_XML, XMLDataProvider.class);

			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(SEEIT3D_EXTENSIONPOINT);
			for (IConfigurationElement element : elements) {
				contributeExternalModelDataProvider(element);
			}
		}
	}

	private static void contributeInternalModelDataProvider(String key, Class<? extends IModelDataProvider> provider) {
		try {
			IModelDataProvider modelDataProvider = provider.newInstance();
			addModelGenerator(key, modelDataProvider);
		} catch (Exception e) {
			ErrorHandler.error(e);
		}
	}

	private static void contributeExternalModelDataProvider(IConfigurationElement configurationElement) {
		String key = configurationElement.getAttribute("id");
		addModelGenerator(key, new PluginProxyModelDataProvider(configurationElement));
	}

	private static void addModelGenerator(String key, IModelDataProvider modelGenerator) {
		if (modelGenerators.containsKey(key)) {
			throw new SeeIT3DException(key + " is already registered in model generators");
		}
		modelGenerators.put(key, modelGenerator);
	}

}
