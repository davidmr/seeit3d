package seeit3d.error.exception;

/**
 * Exception wrapper, it contains a messages that will be displayed to user
 * 
 * @author David
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
