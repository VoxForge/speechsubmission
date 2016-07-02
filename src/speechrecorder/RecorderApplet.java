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
import javax.swing.JApplet;


/**
 * @author  kmaclean
 */
@SuppressWarnings("serial")
public class RecorderApplet extends JApplet {

    static RecorderApplet applet;
    private CapturePlayback theRecorder;
////// MOODLEY:
    String subject;
    String fileFieldName;
    URL endPageURL;
    URL helpPageURL;
    URL destinationURL;
    String language;
    String endpage;
    String helppage;
    String cookie;
    
    public void init() {
        applet = this;
        // At this point grab all our parameters
        getParameters();
        getContentPane().add("Center", 
              theRecorder = new CapturePlayback( language, destinationURL, endPageURL, helpPageURL, cookie));
    }

    public void start() {
        theRecorder.open();
    }

    public void stop() {
        theRecorder.close();
    }

    // Helper method for getting the parameters from the webpage.
    private void getParameters(){
        
        /*  SUBJECT (MOODLEY PARAMETER) */
        try {
            subject = getParameter("subject");
        } catch (NullPointerException nullLang){
            // Default language being set
            subject = null;
            errorMessage(System.out,"subject is null");
        }
        
        try {
        	// !!!!!!        	
        	cookie = getParameter("cookie");
    		// System.err.println("RecorderApplet Cookie: " + cookie +":\n");       
    		// !!!!!!  
        } catch (NullPointerException nullLang){
            errorMessage(System.out,"cookie is null");
        }
        
        /*  LANGUAGE */
        try {
            language = getParameter("language");
            if (language == "" || language == null)
                language = "EN";
           // debug System.err.println("RecorderAppletlanguage is:" + language);
        } catch (NullPointerException nullLang){
            // Default language being set
            language = "EN";
            errorMessage(System.out,"language is null");
        }
        
        /*  DESTINATION  */
        try {
            // !!!!!!        	
        	destinationURL = new URL(getParameter("destination"));
        	// Following line is for testing, and to hard code the applet to postlet.com
        	//    destinationURL = new URL("http://localhost:90/httpd/postlet/javaUpload.php");
        	// !!!!!!
        } catch(java.net.MalformedURLException malurlex){
            // Do something here for badly formed destination, which is ESENTIAL.
            errorMessage(System.out, "Badly formed destination:###"+getParameter("destination")+"###");
     //       JOptionPane.showMessageDialog(null, pLabels.getLabel(3),pLabels.getLabel(5), JOptionPane.ERROR_MESSAGE);
        } catch(java.lang.NullPointerException npe){
            // Do something here for the missing destination, which is ESENTIAL.
            errorMessage(System.out,"destination is null");
     //       JOptionPane.showMessageDialog(null, pLabels.getLabel(4), pLabels.getLabel(5), JOptionPane.ERROR_MESSAGE);
        }
        
        /*  ENDPAGE  */
        try {
            endPageURL = new URL(getParameter("endpage"));
        } catch(java.net.MalformedURLException malurlex){
            errorMessage(System.out, "endpage is badly formed:###"+getParameter("endpage")+"###");
        } catch(java.lang.NullPointerException npe){
            errorMessage(System.out, "endpage is null");
        }
    }

    public void errorMessage(java.io.PrintStream out, String message){
        out.println("***"+message+"***");
    }

}
