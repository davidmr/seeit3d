package seeit3d.colorscale;

import static com.google.common.collect.Lists.*;

import java.util.ArrayList;
import java.util.List;

import seeit3d.colorscale.imp.*;

public class ColorScaleFactory {

	private static ArrayList<IColorScale> allColorScales;

	static {
		allColorScales = newArrayList(
				new BlueTone(),
				new BlueToYellow(),
				new ColdToHotColorScale(),
				new GrayColorScale(), 
				new HeatedObject(),
				new LinearOptimal(), 
				new MagentaTone(),
				new Rainbow());
	}

	public static List<IColorScale> createAllColorScales() {
		return allColorScales;
	}

	public static IColorScale findByName(String colorScaleName) {
		for (IColorScale colorScale : allColorScales) {
			if (colorScale.getName().equals(colorScaleName)) {
				return colorScale;
			}
		}
		return null;
	}
}
