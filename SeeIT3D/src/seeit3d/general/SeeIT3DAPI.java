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
package seeit3d.general;

/**
 * Defines a generic API for each module. Every module that exposes an API must provide only accesors to information, in order to interact with each other must use the EventBus
 * 
 * @author David Montaño
 * 
 */
public interface SeeIT3DAPI {

	// TODO check the use of new, try to use the SeeIT3DFactory
	// TODO reduce the queue event size, it should be smaller. It is getting blocked (bad use of synchronized?)
	// TODO use factory instead of apilocator
}
