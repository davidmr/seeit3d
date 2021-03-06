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

/**
 * Exception wrapper, it contains a messages that will be displayed to user
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DException extends RuntimeException {

	private static final long serialVersionUID = -4990866216441437343L;

	private String showableMessage;

	public SeeIT3DException(String message) {
		showableMessage = message;
	}

	@Override
	public String getMessage() {
		return showableMessage;
	}

}
