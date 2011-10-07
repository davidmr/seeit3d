/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.error.exception;

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
