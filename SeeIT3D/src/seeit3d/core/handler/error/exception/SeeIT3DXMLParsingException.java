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
package seeit3d.core.handler.error.exception;

import javax.xml.bind.JAXBException;

/**
 * Exception to indicate that an error occurred while reading from a XML file due to the file is not correctly formed
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DXMLParsingException extends SeeIT3DException {

	private static final long serialVersionUID = 1L;

	private final JAXBException rootException;

	public SeeIT3DXMLParsingException(JAXBException exception) {
		super("Error reading XML file. Please check that the sintax conforms to specification");
		this.rootException = exception;
	}

	@Override
	public void printStackTrace() {
		rootException.printStackTrace();
	}

}
