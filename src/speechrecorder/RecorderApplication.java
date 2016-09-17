/**
*
* Applet to hold the recorder/uploader panel.
*
* MoodleSpeex written by Dan Stowell.
* (c) 2006 onwards; released under the GPL.
*
* MoodleSpeex uses code from other authors -
* please see the included documentation for
* details.
*
*/

package speechrecorder;

import javax.swing.JFrame;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 
 */
@SuppressWarnings("serial")
public class RecorderApplication extends JFrame {
	private static final File CONFIGURATION_FILE = new File(System.getProperty("user.home"), "VoxForge.properties");

    private CapturePlayback theRecorder;
   	String targetDirectory = ""; // blank target directory means current
    String destination = "http://read.voxforge1.org/r0_2_4b/javaUploadServer.php";
    Locale currentLocale;
    String language="en";
    ResourceBundle messages;
    
    /**
     * constructor
     * 
     * see: https://docs.oracle.com/javase/tutorial/i18n/locale/create.html<br>
     * 
     * Language codes<br>
     * http://www.loc.gov/standards/iso639-2/php/code_list.php
     * 
     * country codes<br>
     * http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
     * 
     * @param args
     */
	public RecorderApplication(String[] args)
	{
	   	//String language="en";
        String country="US";

        currentLocale = null;
		ConfigReader cr=null;
		int languageIndex;
		
		// get default language bundle so can get list of language IDs and
		// language names
    	messages = ResourceBundle.getBundle("speechrecorder/languages/MessagesBundle", new Locale("en"), new UTF8Control() );
		
        if  (args.length == 1) 
        {
            language = new String(args[0]);
            country = null;
            currentLocale = new Locale(language);
        }
        else if  (args.length == 2) 
        {
            language = new String(args[0]);
            country = new String(args[1]);
            currentLocale = new Locale(language, country);
        }
        else if (CONFIGURATION_FILE.exists() && !CONFIGURATION_FILE.isDirectory())
        {
    		try {
    			cr = new ConfigReader(CONFIGURATION_FILE);
    			languageIndex = cr.getInt("language",0);
    			String languageString = CapturePlayback.convertLanguageInd2Lang(messages, languageIndex);
    			language = CapturePlayback.extractLanguageID(languageString);
    			
    			// update resource bundle with langages saved in config file
    	    	messages = ResourceBundle.getBundle("speechrecorder/languages/MessagesBundle", new Locale(language), new UTF8Control() );
    		} catch (IOException e) {
    			currentLocale = new Locale("en");
    		}        	
        }
        else
        {
            currentLocale = new Locale("en");
        }
		
		init();						// simulate browser call(1)
		setSize(800,800);   		// Set the size of the frame
		setVisible(true);   		// Show the frame 
	}
	
    public void init() {
   	
    	theRecorder = new CapturePlayback(language, messages, targetDirectory, destination, CONFIGURATION_FILE ); 
        getContentPane().add("Center", theRecorder);
    }

    public void start() {
        theRecorder.open();
    }

    public void stop() {
        theRecorder.close();
    }
	
    public void errorMessage(java.io.PrintStream out, String message){
        out.println("***"+message+"***");
    }

	/**
	 * 
	 * @param args
	 */
    public static void main(String[] args) 
    {
    	new RecorderApplication(args);
    }
}