/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.xml.modeler.generator.metrics;

import java.util.List;

import seeit3d.base.model.generator.metrics.AbstractNominalMetricCalculator;

/**
 * Categorized metric calculator for XML based visualization. This metric can be instantiated multiple times, one for each time it appears on the XML file
 * 
 * @author David Montaño
 * 
 */
public class XMLNominalMetricCalculator extends AbstractNominalMetricCalculator {

	private static final long serialVersionUID = -7574154133744003269L;

	private final List<String> categories;

	private final int numCategories;

	public XMLNominalMetricCalculator(String name, List<String> categories, int numCategories) {
		super(name);
		this.categories = categories;
		this.numCategories = numCategories;
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	@Override
	public int getNumCategories() {
		return numCategories;
	}

	@Override
	public String calculateMetricValue(Object element) {
		// nothing to do already calculated
		return null;
	}

}