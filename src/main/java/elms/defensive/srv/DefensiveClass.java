package elms.defensive.srv;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author wkjaradat
 *
 */
public final class DefensiveClass {
	/* Class fields */
	// data buffer size set to 2kb
	private static final int MAX_SIZE = 2048;
	// mutable file input stream field
	private static FileInputStream fis = null;
	
	/* Public class constructor which makes defensive copy of file input stream */
	public DefensiveClass(FileInputStream fis) {
		try {
			// defensive copy generated from file descriptor value of input parameter
			this.fis = new FileInputStream(fis.getFD());
		} catch(IOException e) {
			System.err.println("Error generating file input stream.");
		}
	}
	
	/* Public class copy constructor */
	public DefensiveClass(DefensiveClass obj) {
		try {
			this.fis = new FileInputStream(obj.fis.getFD());
		} catch(IOException e) {
			System.err.println("Error generating file input stream.");
		}
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readBytes(FileInputStream is) throws IOException {
		int offset = 0;
		int bytesRead = 0;
		byte[] data = new byte[MAX_SIZE + 1]; //data buffer
		
		// defensively copy file input stream parameter
		final FileInputStream isCopy = new FileInputStream(is.getFD());
		// populate data buffer with bytes read from file input stream copy
		while((bytesRead = isCopy.read(data, offset, data.length - offset)) != -1) {
			offset += bytesRead;
			if(offset >= data.length) {
				throw new IOException("Too much input");
			}
		}
		
		// close file input stream copy object
		isCopy.close();
		// create UTF-8 encoded string data from byte array and return to method caller
		return new String(data, "UTF-8");
	}
}
