package elms.defensive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import elms.defensive.srv.DefensiveClass;

@SpringBootApplication
public final class DefensiveDemo {
	private static Logger log = LoggerFactory.getLogger(DefensiveDemo.class);
	private static final int MAX_NUM_ATTEMPTS = 3;
	
	/**
	 * 
	 * @return
	 */
	private static FileInputStream generateInputStream() {
		FileInputStream fis = null;
		String fileName = null;
		Boolean isValidFile = false; //flag is set to true when a valid file name is specified
		int curNumAttempts = 0;      //tracks the number of times the user enters an invalid file name
		Scanner userInput = new Scanner(System.in);
		
		// read in file name from user via command line
		System.out.print("Enter local file name: ");
		fileName = userInput.nextLine();
		curNumAttempts++;
		do {
			try {
				// obtain file input stream for an existing valid file
				fis = new FileInputStream(fileName);
				// set the valid file flag in order to exit the do-while loop when no exception is thrown
				isValidFile = true;
			} catch(FileNotFoundException e) { //file not found...
				// report exception
				log.error("Error: invalid file name.");
				// prompt user for another file name
				System.out.print("Please enter a valid file name: ");
				// read in file name from user via command line
				fileName = userInput.nextLine();
				curNumAttempts++;
			}
		} while(!isValidFile && curNumAttempts < MAX_NUM_ATTEMPTS);
		
		// if maximum number of attempts are reached, terminate the program to defend against denial of service attacks
		if(curNumAttempts == MAX_NUM_ATTEMPTS) {
			System.out.println("Sorry, you have made too many invalid entries. Program will now terminate.");
			// terminate program
			Runtime.getRuntime().exit(1);
		}
		
		// close scanner object
		userInput.close();
		// confirm that isValidFile flag is actually set (for defensive purposes)
		assert(isValidFile) : "Assertion failed: the isValidFile flag has actually not been set.";
		return fis;
	}
	
	public static void main(String[] args) {
		// read in file name and generate file input stream
		FileInputStream fis = generateInputStream();
		// instantiate DefensiveClass with regular constructor
		DefensiveClass dObj = new DefensiveClass(fis);
		// call copy constructor
		DefensiveClass dObjCopy = new DefensiveClass(dObj);
		
		// use copy object to read data from input stream and convert to string
		try {
			String str = dObjCopy.readBytes(fis);
			System.out.println("Contents of file: ");
			System.out.println(str);
		} catch(Exception e) {
			log.error("Error reading data.");
		} finally { //close all resources...
			try {
				fis.close();
				System.out.println("Successfully closed file input stream.");
			} catch(Exception e) {
				log.error("Error closing file input stream.");
			}
		}
		
		// terminate program
		System.out.println("Program execution complete, exiting now...");
		Runtime.getRuntime().exit(1);
	}
}
