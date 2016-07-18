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
import java.net.URL;

import javax.swing.JFrame;


/**
 * 
 */
@SuppressWarnings("serial")
public class RecorderApplication extends JFrame {

    private CapturePlayback theRecorder;
    String subject;
    String fileFieldName;
    String language = "EN";
   	String targetDirectory = ""; // blank target directory means current
    String destination = "http://read.voxforge1.org/r0_2_4b/javaUploadServer.php";
   	
	public RecorderApplication()
	{
		System.out.println("Java");
		init();				// simulate browser call(1)
		setSize(800,800);   		// Set the size of the frame
		setVisible(true);   		// Show the frame 
	}
	
    public void init() {
    	theRecorder = new CapturePlayback( language, targetDirectory, destination); 
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
