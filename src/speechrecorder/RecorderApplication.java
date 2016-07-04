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

import java.awt.Container;
import javax.swing.JFrame;


/**
 * 
 */
@SuppressWarnings("serial")
public class RecorderApplication extends JFrame {

    private CapturePlayback theRecorder;
////// MOODLEY:
    String subject;
    String fileFieldName;
    String language = "EN";
    
	public RecorderApplication()
	{
		System.out.println("Java");
		init();				// simulate browser call(1)
		setSize(800,1100);   		// Set the size of the frame
		setVisible(true);   		// Show the frame 
	}
    
    public void init() {
    	theRecorder = new CapturePlayback( language, "/home/kmaclean/temp/");
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

    
    public static void main(String[] args) 
    {
    	        new RecorderApplication();
    }
}
