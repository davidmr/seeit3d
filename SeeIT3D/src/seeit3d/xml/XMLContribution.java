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
package seeit3d.xml;

import seeit3d.base.ISeeIT3DContributor;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The XML contribution to SeeIT 3D
 * 
 * @author David Montaño
 * 
 */
public class XMLContribution implements ISeeIT3DContributor {

	private static Injector injector;

	@Override
	public void initialize() {
		try {
			injector = Guice.createInjector(new XMLModule());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Injector injector() {
		checkInitialized();
		return injector;
	}

	private static void checkInitialized() {
		if (injector == null) {
			throw new IllegalArgumentException("XML is not initialized");
		}
	}

}
