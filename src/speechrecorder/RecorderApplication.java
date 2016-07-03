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

import java.net.URL;

import javax.swing.JFrame;


/**
 * @author  kmaclean
 */
@SuppressWarnings("serial")
public class RecorderApplication extends JFrame {

   // static RecorderApplet applet;
    private CapturePlayback theRecorder;
////// MOODLEY:
    String subject;
    String fileFieldName;
    URL endPageURL;
    URL helpPageURL;
    URL destinationURL;

    String language = "EN";
    String endpage = "www.voxforge.org";
    String helppage = "www.voxforge.org";
    String cookie = null;
    
	public RecorderApplication()
	{
		System.out.println("Java");
		init();				// simulate browser call(1)
		setSize(400,600);   		// Set the size of the frame
		setVisible(true);   		// Show the frame 
	}
    
    public void init() {
    	
        getContentPane().add("Center", 
              theRecorder = new CapturePlayback( language, "/home/kmaclean/temp/", cookie));
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
