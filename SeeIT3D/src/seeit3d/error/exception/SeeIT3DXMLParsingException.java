package seeit3d.error.exception;

import javax.xml.bind.JAXBException;

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
