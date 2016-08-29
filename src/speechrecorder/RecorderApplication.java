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
import java.util.*;

/**
 * 
 */
@SuppressWarnings("serial")
public class RecorderApplication extends JFrame {

    private CapturePlayback theRecorder;
   	String targetDirectory = ""; // blank target directory means current
    String destination = "http://read.voxforge1.org/r0_2_4b/javaUploadServer.php";
    ResourceBundle messages;
    Prompts prompts;
    static int numberOfPrompts = 3;
    
	public RecorderApplication(ResourceBundle messages, Prompts prompts)
	{
		this.messages = messages;
		this.prompts = prompts;
		init();						// simulate browser call(1)
		setSize(800,800);   		// Set the size of the frame
		setVisible(true);   		// Show the frame 
	}
	
    public void init() {
    	theRecorder = new CapturePlayback( messages, prompts, targetDirectory, destination ); 
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
    public static void main(String[] args) 
    {
    	String language="en";
        String country="US";

        Locale currentLocale;
        ResourceBundle messages;
        Prompts prompts;
        
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
        else
        {
            currentLocale = new Locale("en");
        }
        
	    prompts = new Prompts(language, numberOfPrompts);
	    messages = ResourceBundle.getBundle("speechrecorder/languages/MessagesBundle", currentLocale, new UTF8Control() );
        
    	new RecorderApplication(messages, prompts);
    }
}
