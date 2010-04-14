package seeit3d.colorscale;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import seeit3d.colorscale.imp.BlueToYellow;
import seeit3d.colorscale.imp.BlueTone;
import seeit3d.colorscale.imp.ColdToHotColorScale;
import seeit3d.colorscale.imp.GrayColorScale;
import seeit3d.colorscale.imp.HeatedObject;
import seeit3d.colorscale.imp.LinearOptimal;
import seeit3d.colorscale.imp.MagentaTone;
import seeit3d.colorscale.imp.Rainbow;

public class ColorScaleFactory {
	
	public static List<IColorScale> createAllColorScales(){
		return newArrayList(new BlueTone(),
				new BlueToYellow(),
				new ColdToHotColorScale(),
				new GrayColorScale(),
				new HeatedObject(),
				new LinearOptimal(),
				new MagentaTone(),
				new Rainbow()
				);
	}
}
