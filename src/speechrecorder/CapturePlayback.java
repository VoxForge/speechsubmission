package speechrecorder;
/**
* 
* Note: This is a copy of the CapturePlayback class provided in the 
* JavaSoundDemo, but modified for use with the SpeechRecorder 
* applet.
* 
*/


/*
 * @(#)CapturePlayback.java	1.11	99/12/03
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import speechrecorder.ConfigReader;
import speechrecorder.SaveOrUpload;
import speechrecorder.SamplingGraph;
import speechrecorder.Capture;
import speechrecorder.Playback;

/**
 * Capture/Playback sample.  Record audio in different formats and then playback the recorded audio.  The captured audio can  be saved either as a WAVE, AU or AIFF.  Or load an audio file for streaming playback.
 * @version  @(#)CapturePlayback.java	1.11	99/12/03
 * @author  Brian Lichtenwalter
 */

@SuppressWarnings("serial")
public class CapturePlayback extends JPanel implements ActionListener, net.sf.postlet.PostletInterface {

	private static final File CONFIGURATION_FILE = new File(System.getProperty("user.home"), "VoxForge.properties");

    final int bufSize = 16384;
    public static int BUFFER_SIZE = 10240;
    public static final String fileType = "wav";     
    public static final int samplingRate = 48000;// jre 1.4.2 only supports max of 44100
    public static final int samplingRateFormat = 16;      
    public static final int numberChannels = 1;      

    AudioFormat format = new AudioFormat(samplingRate, samplingRateFormat, numberChannels, true, false);

    Capture capture;
    Playback playback;
    CapturePlayback capturePlayback; // Needed for referencing within the inner classes

    AudioInputStream audioInputStream;
    SamplingGraph samplingGraph;

    JButton [] playA; 
    JButton [] captA;

    JButton uploadB;
    //JButton saveLocalB;
    JButton moreInfoB;    
    JButton aboutB; 

    boolean [] play_stateA;
    boolean [] capt_stateA;

    JTextField textField;

    String fileName = "untitled";
    String errStr;

    double [] durationA;
    double duration = 0;

    double seconds;

    long [] totalBytesWrittenA;
    long totalBytesWritten = 0L;
     
    File file;
  
    private File wavFile;
    private File[] wavFileA; // raw audio
    private File [] uploadWavFileA; // wav file with header

    JProgressBar progBar;
    int sentBytes;
    int totalBytes;
    int buttonClicked;

    JTextField subjectBox;
    String subject;

    String [] promptA;
    String [] promptidA;
    
//  ############ Localized Fields ####################################   
    JTextField usernameTextField;  
	JComboBox<String[]> languageChooser;       
    String language = "en";
    
    String userName = "unknown";
	JComboBox<String[]> genderChooser;       
    String gender;
    JComboBox<String[]> ageRangeChooser; 
    String ageRange;
    JComboBox<String[]> dialectChooser;
    String dialect;  
    JComboBox<String[]> microphoneChooser;     
    String microphone;  
//  ############ Localized Fields ####################################   

    String targetDirectory;
    URL destinationURL;
    
    String cookie;
    
    String licenseNotice;
    String vflicense;
  
	String tempdir;

	SaveOrUpload saveOrUpload; 
	//ConvertAndSavelocally convertAndSavelocally; 
	
    Color voxforgeColour = new Color(197, 216, 234);
    
    ResourceBundle messages;
    Prompts prompts;
    Boolean rightToLeft = false;
    
    // constructor
    //public CapturePlayback(String lang, String targetDirectory, String destination) 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public CapturePlayback( Locale currentLocale, String targetDirectory, String destination) 
	{    	
		this.capturePlayback = this;
    	this.targetDirectory = targetDirectory;
        try 
        {
        	this.destinationURL = new URL(destination);
        } 
        catch(java.net.MalformedURLException malurlex)
        {
            System.err.println( "Badly formed destination URL: " + destination);
        } 
        catch(java.lang.NullPointerException npe)
        {
            System.err.println( "destination is null" );
        }
        int numberOfPrompts = 3;
        prompts = new Prompts(language, numberOfPrompts);
	    messages = ResourceBundle.getBundle("speechrecorder/languages/MessagesBundle", currentLocale, new UTF8Control() );
        
        tempdir = getTempDir(); 
		
		initPromptNumArrays(prompts.getNumberOfPrompts());
		
		rightToLeft = messages.getString("rightToLeft").equals("true") ?  true :  false;
		languageDependent(messages);
	    
        
		JPanel userPanel = startApp();
		
		JScrollPane scrollPane = new JScrollPane(userPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(300, 300));
		
        //loadSettings();
	}

	// methods
    /**
     * see http://stackoverflow.com/questions/14874613/how-to-replace-jpanel-with-another-jpanel
     */
    private JPanel startApp() 
    { 	
		JPanel userPanel = new JPanel();
        
		addLangSel(userPanel);
		
        addUserInfo(userPanel);
        addPromptInfo(userPanel);

	    createWavFiles(prompts.getNumberOfPrompts(), this.promptidA ); // promptidA array gets assigned in addPromptInfo
        addGraph(userPanel); 
        addRemainingPanelInfo(userPanel); 

        return userPanel;
    }	
    
	/**
	 * see http://stackoverflow.com/questions/14874613/how-to-replace-jpanel-with-another-jpanel
	 */
    private void restartApp() 
    { 	
		removeAll();  //Removes all the components from this container
        tempdir = getTempDir(); // creates new temp dir with every call

        int numberOfPrompts = prompts.getNumberOfPrompts();
        prompts = new Prompts(language, numberOfPrompts);
        messages = ResourceBundle.getBundle("speechrecorder/languages/MessagesBundle", new Locale(language), new UTF8Control() );
        
        languageDependent(messages);
        
		JPanel userPanel = startApp();
		
		JScrollPane scrollPane = new JScrollPane(userPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(300, 300));
        
        validate();
        repaint();
        setVisible(true);
    }	
    
    
    /**
     * initialize arrays whose size is dependent on the number of prompts
     * the user has selected
     * 
     */
    private void initPromptNumArrays(int numberofPrompts) 
    { 	
	    playA = new JButton [numberofPrompts]; 
	    captA = new JButton [numberofPrompts];

	    play_stateA = new boolean [numberofPrompts];
	    capt_stateA = new boolean [numberofPrompts];

	    durationA= new double [numberofPrompts];
	    totalBytesWrittenA= new long [numberofPrompts];
	    wavFileA = new File [numberofPrompts]; // raw audio
	    uploadWavFileA = new File [numberofPrompts]; // wav file with header

	    promptA = new String [numberofPrompts];
	    promptidA = new String [numberofPrompts];
    }
    
    
    /**
     * updates language dependent objects and variables
     * 
     * message object is language dependent
     * 
     * @param language
     */
    private void languageDependent(ResourceBundle messages) 
    { 	
    	Calendar cal = Calendar.getInstance();
		licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + messages.getString("copyrightName") + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") 
				+ License.getBlanklicenseNotice();				
		vflicense = License.getVFLicense();	 	

		saveOrUpload = new SaveOrUpload(
				capturePlayback,
				destinationURL, 
				messages.getString("uploadingMessageLabel"),
				prompts.getNumberOfPrompts(),
				uploadWavFileA,
			    promptidA,
			    promptA,
			    tempdir,
			    licenseNotice,
			    BUFFER_SIZE
		);
		//convertAndSavelocally = new ConvertAndSavelocally();
		
	    gender = messages.getString("notApplicable"); // default selection
	    ageRange = messages.getString("notApplicable"); // default selection
	    dialect = messages.getString("notApplicable");  // default selection
	    microphone = messages.getString("notApplicable");  // default selection
	    
	    playback = new Playback(
		    	capturePlayback,
		    	format,
        		prompts.getNumberOfPrompts(),
                bufSize,
                messages.getString("peakWarningLabel"),
                messages.getString("sampleGraphFileLabel"),
                messages.getString("sampleGraphLengthLabel"),
                messages.getString("sampleGraphPositionLabel"),
                messages.getString("playButton"),
                messages.getString("stopButton")
	    );
	    
	    capture = new Capture(
	    	capturePlayback,
	    	format,
            messages.getString("peakWarningLabel"),
            messages.getString("sampleGraphFileLabel"),
            messages.getString("sampleGraphLengthLabel"),
            messages.getString("sampleGraphPositionLabel")
	    );
    }
    
    /**
     * extracts two character Language ID from language string<br>
     * e.g. EN - English - will extract 'EN'
     * 
     * @param languageString
     * @return
     */
	private String extractLanguageID(String languageString) 
    { 
		return languageString.split("\\s*-\\s*")[0];
    }
    
    /**
     * add language selector
     * 
     * @param p2
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addLangSel(JPanel p2) 
    { 
        JPanel languagePanel = new JPanel();
        languagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));  
        
		System.out.println("language:" + language);

        if (rightToLeft)
        {
           	languagePanel.add( languageChooser = new JComboBox( convertLanguage2Array("languageSelection") ) ); 
        	languagePanel.add(new JLabel(messages.getString("languagePanelLabel")));       	
        }
        else
        {
           	languagePanel.add(new JLabel(messages.getString("languagePanelLabel")));       	
           	languagePanel.add( languageChooser = new JComboBox( convertLanguage2Array("languageSelection") ) );
        }
        
        languageChooser.setSelectedIndex(0); 
        languageChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					String languageString = (String)languageChooser.getSelectedItem();
					
					System.err.println("languageString " + languageString);
					
                    if ( ! languageString.equals(messages.getString("pleaseSelect")) )
	                {
                    	 language = extractLanguageID(languageString);
	                     System.out.println("changing language to: " + language);
	        			 restartApp();
                    }
				}
        	});
        p2.add(languagePanel);
    }
    
	/**
	 * add prompts to GUI
	 * 
	 * @param userPanel
	 * @param numberofPrompts
	 */
    private void addPromptInfo(JPanel userPanel) 
    { 
    	JPanel promptsContainer = new JPanel();
	    String [][] promptArray = prompts.getPrompts();
	    int numberofPrompts = prompts.getNumberOfPrompts();
	    for (int i = 0; i < numberofPrompts; i++) 
	    {
	    	this.promptidA[i] = promptArray[0][i];
	    	this.promptA[i] = promptArray[1][i];
	    }
    	
    	//		############ Prompt container ####################################   

        promptsContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        int startPromptCount = 0;
        int promptsPerPane = numberofPrompts;
	
        //      ############ Prompts panel ####################################         
        JPanel prompts = new JPanel(); 
        prompts.setLayout(new BoxLayout(prompts, BoxLayout.Y_AXIS));
        prompts.setBorder(BorderFactory.createLineBorder (voxforgeColour, 3));
    
        int maxWidth = 40;

        JPanel promptPanelA[] = new JPanel[promptsPerPane]; 
        JPanel promptInnerPanelA[] = new JPanel[promptsPerPane]; 
        for (int i = startPromptCount; i < promptsPerPane; i++) 
        {
        	promptPanelA[i] = new JPanel();
        	promptPanelA[i].setLayout(new FlowLayout(FlowLayout.RIGHT));
        	promptInnerPanelA [i]= new JPanel();
	        promptInnerPanelA[i].setBorder(BorderFactory.createLineBorder (voxforgeColour, 1));
	        promptInnerPanelA[i].add(new MultiLineLabel(promptPanelA[i], this.promptA[i], maxWidth, rightToLeft));
	        promptPanelA[i].add(promptInnerPanelA[i]);
	        playA[i] = addButton(messages.getString("playButton"), promptPanelA[i], false);
	        if (i==0) {
	        	captA[i] = addButton(messages.getString("recordButton"), promptPanelA[i], true); // only turn on first record button 
	        } else {
		        captA[i] = addButton(messages.getString("recordButton"), promptPanelA[i], false);
	        }
	        prompts.add(promptPanelA[i]);  
        }
        
		//############ Prompt container ####################################   	
        promptsContainer.add(prompts);

        userPanel.add(promptsContainer);
    }	
	    
    /**
     * Create WAV files to hold recordings
     * 
     * @param numberofPrompts
     * @param promptidA
     */
    private void createWavFiles(int numberofPrompts, String [] promptidA ) 
    { 
		try {
	        for (int i = 0; i < numberofPrompts; i++) 
	        {
	        	wavFileA[i] = new File(tempdir + "wavFile" + i + ".wav");
	        	wavFileA[i].deleteOnExit();
	        }
	        for (int i = 0; i < numberofPrompts; i++) 
	        {
				uploadWavFileA[i] = new File(tempdir + promptidA [i] + ".wav");
				uploadWavFileA[i].deleteOnExit();
	        }
		} catch (Exception e) {
			System.err.println("Unable to create WAV cache file for storing audio\n" + e);
			return;
		}
	    for (int i = 0; i < numberofPrompts; i++) 
	    {			
			System.out.println("CapturePlayback's WAV file for recording uploadWavFile" + i + " is: " + uploadWavFileA[i]);
			//System.err.println("CapturePlayback's raw file for recording wavFileA" + i + " is: " + wavFileA[i]);
	    }    	
    }
    
    /**
     * Sampling Graph
     * progress bar
     * 
     * @param userPanel
     */
    private void addGraph(JPanel userPanel) 
    { 
        SoftBevelBorder sbb = new SoftBevelBorder(SoftBevelBorder.LOWERED);
        EmptyBorder eb = new EmptyBorder(10,20,5,20);

        JPanel samplingPanel = new JPanel(new BorderLayout());
        samplingPanel.setBorder(new CompoundBorder(eb, sbb));
        
        samplingGraph = new SamplingGraph(
        				capture,
        				playback,
        				messages.getString("sampleGraphFileLabel"),
        				messages.getString("sampleGraphLengthLabel"),
        				messages.getString("sampleGraphPositionLabel")
        );
        samplingGraph.setPreferredSize(new Dimension(50, 100));
        samplingPanel.add(samplingGraph);
        
        userPanel.add(samplingPanel);
        
    	//		############ Upload Progress bar ####################################
        progBar = new JProgressBar();
        progBar.setStringPainted(true);
        progBar.setString(messages.getString("readyToRecord"));
        userPanel.add(progBar);    
    }
    
    /**
     * Add remaining Panel Information
     * - upload text & button
     * - upload progress bar
     * - more information button
     * - disclaimer
     * 
     * @param p2
     */
    private void addRemainingPanelInfo(JPanel p2) 
    { 
	//      ############ Upload Text ####################################             
        JPanel uploadTextPanel = new JPanel();
        uploadTextPanel.add(new JLabel(messages.getString("uploadText")) ); 
        
        p2.add(uploadTextPanel);
	//		############ Upload ####################################          
        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.setBorder(new EmptyBorder(5,0,5,0));
        uploadB = addButton(messages.getString("uploadButtonLabel"), uploadButtonPanel, false); // upload all submissions
        p2.add(uploadButtonPanel);
    	//		############ Save Local ####################################          
        //saveLocalB = addButton("save on your computer", uploadButtonPanel, false); // upload all submissions
        //p2.add(uploadButtonPanel);
           
	// 		############ More Information Button ####################################          
        JPanel moreInfoButtonPanel = new JPanel();
        if (rightToLeft)
        {
       	    moreInfoB = addButton(messages.getString("moreInfoButtonLabel"), moreInfoButtonPanel, true); 
            moreInfoButtonPanel.add(new JLabel(messages.getString("moreInfoText"))); 
        }
        else
        {
	        moreInfoButtonPanel.add(new JLabel(messages.getString("moreInfoText")));
	        moreInfoB = addButton(messages.getString("moreInfoButtonLabel"), moreInfoButtonPanel, true);     	 
        }
        p2.add(moreInfoButtonPanel);   
// 		############ Disclaimer ####################################  
        JPanel AboutPanel = new JPanel();
        AboutPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JPanel AboutInnerPanel = new JPanel(); 
    	aboutB = addButton(messages.getString("aboutButtonLabel"), AboutInnerPanel, true); 
    	AboutInnerPanel.setBorder(BorderFactory.createLineBorder (voxforgeColour, 3));
    	AboutPanel.add(AboutInnerPanel);        
        p2.add(AboutPanel); 
	//#########################################################################   
    }
	
    /**
     * convert a localizable, comma separated, string into a string array
     * 
     * @param key
     * @return
     */
	private String[] convertMessage2Array(String key) 
    {
		String [] result;
		
       	String [] messageArray = (messages.getString(key)).split("\\s*,\\s*");
       	
       	result = new String[messageArray.length + 1];
       	result[0] = messages.getString("pleaseSelect");
       	for (int i=0; i<messageArray.length; i++)
        {
          result[i+1] = messageArray[i];
        }
       	
       	return result;
    }

    /**
     * convert comma separated Language List string into a string array
     * 
     * @param key
     * @return
     */
	private String[] convertLanguage2Array(String key) 
    {
       	return (messages.getString(key)).split("\\s*,\\s*");
    }	
	
    /**
     * User name,
     * Gender,
     * Age Range,
     * Pronunciation Dialect,
     * Microphone Type
     **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void addUserInfo(JPanel p2) 
    { 
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
	// 		############ User name ####################################             
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        if (rightToLeft)
        {
            usernamePanel.add(usernameTextField = new JTextField(20));        	
            usernamePanel.add(new JLabel(messages.getString("usernamePanelLabel")));  
        }
        else
        {
	        usernamePanel.add(new JLabel(messages.getString("usernamePanelLabel")));
	        usernamePanel.add(usernameTextField = new JTextField(20));     	
        }
        usernamePanel.add(new JLabel(messages.getString("usernamePanelText")));     
        p2.add(usernamePanel);  
    // 		############ Gender ####################################             
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  
        if (rightToLeft)
        {
	        genderPanel.add(genderChooser = new JComboBox( convertMessage2Array("genderSelection")  ));
        	genderPanel.add(new JLabel(messages.getString("genderPanelLabel")));
        }
        else
        {
	        genderPanel.add(new JLabel(messages.getString("genderPanelLabel")));
	        genderPanel.add(genderChooser = new JComboBox( convertMessage2Array("genderSelection") ));
        }
        genderChooser.setSelectedIndex(0);       
        genderChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
                     gender = (String)genderChooser.getSelectedItem();
                 }
        	});
        p2.add(genderPanel);
	// 		############ Age Range ####################################             
        JPanel ageRangePanel = new JPanel();
        ageRangePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        if (rightToLeft)
        {
			ageRangePanel.add(ageRangeChooser = new JComboBox(convertMessage2Array("ageSelection")) );
	       	ageRangePanel.add(new JLabel(messages.getString("ageRangePanelLabel")));
        }
        else
        {
        	ageRangePanel.add(new JLabel(messages.getString("ageRangePanelLabel")));
			ageRangePanel.add(ageRangeChooser = new JComboBox(convertMessage2Array("ageSelection")) );
        }
        ageRangeChooser.setSelectedIndex(0);          
        ageRangeChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
                     ageRange = (String)ageRangeChooser.getSelectedItem();
                 }
        	});
        p2.add(ageRangePanel);
	//      ############ Pronunciation Dialect ####################################       
        JPanel dialectPanel = new JPanel();
        dialectPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        if (rightToLeft)
        {
        	dialectPanel.add(dialectChooser = new JComboBox(convertMessage2Array("dialectSelection")));    
        	dialectPanel.add(new JLabel(messages.getString("dialectPanelLabel")));
        }
        else
        {
        	dialectPanel.add(new JLabel(messages.getString("dialectPanelLabel")));
        	dialectPanel.add(dialectChooser = new JComboBox(convertMessage2Array("dialectSelection")));
        }
        dialectChooser.setSelectedIndex(0);  
        dialectChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
	                dialect = (String)dialectChooser.getSelectedItem();
	            }
            });
        p2.add(dialectPanel);
	//      ############ Microphone Type ####################################       
        JPanel microphonePanel = new JPanel();
        microphonePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        if (rightToLeft)
        {
	        microphonePanel.add(microphoneChooser = new JComboBox(convertMessage2Array("microphoneSelection")));
	        microphonePanel.add(new JLabel(messages.getString("microphonePanelLabel")));       
        }
        else
        {
        	microphonePanel.add(new JLabel(messages.getString("microphonePanelLabel")));
	        microphonePanel.add(microphoneChooser = new JComboBox(convertMessage2Array("microphoneSelection"))); 
        }
        microphoneChooser.setSelectedIndex(0);  
        // microphoneChooser.setEditable(true); // user can add whatever they want ...
        microphoneChooser.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
                     microphone = (String)microphoneChooser.getSelectedItem();
                 }
        	});
        p2.add(microphonePanel);
    }	

    /**
     * create temporary directory and return path as a string; 
     * 
     * creates new temp dir with every call
     * 
     * @return
     */
    private String getTempDir() {
    	String tempdir=null;
		try {
	    	File dir=File.createTempFile("VF-dir",null);
	    	dir.delete();
	    	dir.mkdir();
	    	tempdir = dir.toString();
	    	if ( !(tempdir.endsWith("/") || tempdir.endsWith("\\")) )
	    		   tempdir = tempdir + System.getProperty("file.separator");

		} catch (Exception e) {
			System.err.println("Unable to create temp directory\n" + e);
		}
		return tempdir;
    }
	
    public void open() { }
    
    public void close() {
        if (playback.thread != null) {
            for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
            	playA[i].doClick(0);
            }
        }
        if (capture.thread != null) {
            for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
            	captA[i].doClick(0);
            }
        }
    }

    private JButton addButton(String name, JPanel p, boolean state) {
        JButton b = new JButton(name);
        b.addActionListener(this);
        p.add(b);
        b.setEnabled(state);
        return b;
    }
    
    private void setButtonsOff() {
        for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
        	playA[i].setEnabled(false); 
        	captA[i].setEnabled(false); 
        }
    }
    
    private void saveButtonState() {
        for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
        	if (playA[i].isEnabled()) {play_stateA [i] = true;} else {play_stateA [i] = false;}
        	if (captA[i].isEnabled()) {capt_stateA [i] = true;} else {capt_stateA [i] = false;}
        }
    }
    
    protected void restoreButtonState() {
        for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
         	if (play_stateA[i]) {playA[i].setEnabled(true);} else {playA[i].setEnabled(false);}
        	if (capt_stateA[i]) {captA[i].setEnabled(true);} else {captA[i].setEnabled(false);}
        }
    }

    /**
     * Which button pressed:
     * 
     *  Play
     *  Record
     *  Upload
     *  More Information
     *  About
     *  
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
// ################### Play #######################################       
        for (int i = 0; i < prompts.getNumberOfPrompts(); i++) {
            if (obj.equals(playA[i])) {
                if (playA[i].getText().startsWith(messages.getString("playButton"))) {
                    wavFile = wavFileA[i];      
                    duration = durationA[i];
                    totalBytesWritten = totalBytesWrittenA[i];
                    System.out.println("=== Play " + (i+1) + " ===");

                    fileName = promptidA[i];  
                    playback.start(
	                		samplingGraph,
	                		progBar,
	                        playA, 
	                        captA,
	                        promptidA[i],
	                        duration
                    );
            		System.out.println("duration:" + duration);

                    samplingGraph.start();
	                saveButtonState(); 
	                setButtonsOff();
                    captA[i].setEnabled(false);
                    playA[i].setEnabled(true);
                    playA[i].setText(messages.getString("stopButton"));
                } else {
                    playback.stop();
                    samplingGraph.stop();
	                restoreButtonState(); 
                    captA[i].setEnabled(true);
                    playA[i].setText(messages.getString("playButton"));
                }
            }
        }

// ################### Record (capture) #######################################        
	    for (int x = 0; x < prompts.getNumberOfPrompts(); x++) {
	        if (obj.equals(captA[x])) {
	            if (captA[x].getText().startsWith(messages.getString("recordButton"))) {
	                file = null;
	                wavFile = wavFileA[x];  
	                fileName = promptidA[x];
	        		System.out.println("=== Record " + (x+1) + " ==="); 
	                capture.start(
	                		samplingGraph,
	                		progBar,
	                		uploadWavFileA[x],
	                		wavFileA[x],
	                		promptidA[x]
	                );  
	                samplingGraph.start();
	                saveButtonState(); 
	                setButtonsOff(); 
	                captA[x].setEnabled(true);    
	                captA[x].setText(messages.getString("stopButton"));
	                moreInfoB.setEnabled(false);  
	                aboutB.setEnabled(false); 
	            } else {
	            	samplingGraph.removeAllLinesElements();
	                try {  
	                	Thread.sleep(1000);
	                } catch (InterruptedException ex) { 
	        			System.out.println("Recording Thread - Interrupt Exception");
	                }
	                
	                CaptureResult result = capture.stop();
//	                audioInputStream = result.audioInputStream;
	                duration = result.duration;
	                totalBytesWritten = result.totalBytesWritten;
	                
	                totalBytesWrittenA[x] = totalBytesWritten; 
	            	durationA[x]= totalBytesWritten / (double) (format.getSampleRate() * format.getSampleSizeInBits()/ 8);
	        		System.out.println("duration1:" + durationA[x]);
	            	samplingGraph.stop();
	                restoreButtonState(); 
	                playA[x].setEnabled(true);
	                captA[x].setText(messages.getString("recordButton"));
	                moreInfoB.setEnabled(true);  
	                aboutB.setEnabled(true); 
	                captA[x].setEnabled(true);
	                if (x < prompts.getNumberOfPrompts()-1) {
	                	captA[x+1].setEnabled(true);
	                }
	        		//System.out.println("x " + x +"numberofPrompts " + prompts.getNumberOfPrompts());
	                if (x == prompts.getNumberOfPrompts()-1) {
	                	uploadB.setEnabled(true);
	                	//saveLocalB.setEnabled(true);
	                }
	            }
	        } 
	    }

//          ################### Upload #######################################               
	    if (obj.equals(uploadB)) 
	    { 
	        for (int i = 0; i < prompts.getNumberOfPrompts(); i++) 
	        {
	        	playA[i].setEnabled(false);
	            captA[i].setEnabled(false);
	        }
            uploadB.setEnabled(false);               
        	try 
        	{
 	    	   usernameTextField.selectAll();
	    	   userName = usernameTextField.getText();
    		   // see   java.util.regex.Pattern: \W  A non-word character: [^\w]
			   userName = (usernameTextField.getText().replaceAll("\\W",""));
	    	   if (userName.length() == 0 ) 
	    	   {
	               userName = "anonymous";
	    	   } else 
	    	   {
				   if (userName.length() > 40 ) 
				   {
					   userName = userName.substring(0,40);
	    		   } 
	    	   }
        	} 
        	catch (NullPointerException ex) 
        	{ 
               userName = "anonymous";
            }

			//saveSettings();
        	/*
       		totalBytes = saveOrUpload.start(
							progBar, 
							language, 
							userName, 
							userDataToString() 
			);
			*/
        	totalBytes = saveOrUpload.createArchive(
					progBar, 
					language, 
					userName, 
					userDataToString() 
			);
        	
        	
        	saveOrUpload.upload();
        	
			restartApp();
        }
//      ################### SaveLocally #######################################   
	    /*
	    if (obj.equals(saveLocalB)) 
	    { 
	        for (int i = 0; i < numberofPrompts; i++) 
	        {
	        	playA[i].setEnabled(false);
	            captA[i].setEnabled(false);
	        }
	        saveLocalB.setEnabled(false);               
	    	try 
	    	{
		    	   usernameTextField.selectAll();
	    	   userName = usernameTextField.getText();
			   userName = (usernameTextField.getText().replaceAll("\\W",""));
	    	   if (userName.length() == 0 ) 
	    	   {
	               userName = "anonymous";
	    	   } 
	    	   else 
	    	   {
				   if (userName.length() > 40 ) 
				   {
					   userName = userName.substring(0,40);
	    		   } 
	    	   }
	    	} 
	    	catch (NullPointerException ex) 
	    	{ 
	           userName = "anonymous";
	        }
	
			saveSettings();
	 	
			convertAndSavelocally.start(targetDirectory);
			restartApp();
	    }	   
	    */ 
//      ################### More Information #######################################     
        else if (obj.equals(moreInfoB)) {
         	 JTextArea textArea = new JTextArea(License.getLicense());
             textArea.setLineWrap(true);
             textArea.setWrapStyleWord(true);
             JScrollPane areaScrollPane = new JScrollPane(textArea);
             areaScrollPane.setVerticalScrollBarPolicy(
             		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
             areaScrollPane.setPreferredSize(new Dimension(600, 600));
  
            JOptionPane.showMessageDialog(this, areaScrollPane, 
                    "More info on Copyright and GPL license", JOptionPane.PLAIN_MESSAGE);
        }
//      ################### About ####################################### 
        else if (obj.equals(aboutB)) {
        	JTextArea textArea = new JTextArea(vflicense);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            areaScrollPane.setVerticalScrollBarPolicy(
            		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            areaScrollPane.setPreferredSize(new Dimension(600, 600));
 
           JOptionPane.showMessageDialog(this, areaScrollPane, 
                   "About VoxForge Speech Submission Application", JOptionPane.PLAIN_MESSAGE);
       }
    }

    /**
     *  return multiple values from Capture class
     * 
     * @author daddy
     *
     */
    protected class CaptureResult {
    	double duration;
    	long totalBytesWritten;
    }
    
    /**
	 * Will free audio input stream if exists, and grab a new version. 
	 * This seems a bit silly but there's no other way to "rewind" large files.
	 * 
	 * @uml.property  name="audioInputStream"
	 */
    protected AudioInputStream getAudioInputStream(){
		if (audioInputStream != null) {
			try {
				audioInputStream.close();
			} catch (IOException err) {
			}
		}
		System.out.println("getAudioInputStream - totalBytesWritten:" + totalBytesWritten);
		try {
			audioInputStream = new AudioInputStream(new BufferedInputStream(
					new FileInputStream(wavFile)), format, totalBytesWritten
					/ (format.getChannels() * format.getSampleSizeInBits() / 8) // Length in sample frames
			);

 			
		} catch (Exception err) {
			System.err.println("Exception while reading cache file: " + err);
		}
		System.out.println("Grabbed audio input stream from cache file");
		
		return audioInputStream;
	}

    /**
     * called by Postlet uploader
     */
 	public synchronized void setProgress(int a) {
         sentBytes += a;
         progBar.setValue(sentBytes);
         
         if (sentBytes == totalBytes)
         {
            progBar.setStringPainted(true);
           	progBar.setString(messages.getString("uploadCompletedMessageLabel"));
            progBar.setIndeterminate(false);
            System.out.println("Finished! submission uploaded to VoxForge repository");
            // Reset the applet
            progBar.setValue(0);
            sentBytes = 0;
         }
         else 
         {
         	//FORDEBUG
         	System.err.println("setProgress(): Not reached end yet. sentBytes="+sentBytes+", totalBytes="+totalBytes);
         }
 	}    
    
    /**
     * save speech submission settings to user's computer
     */
    public void saveSettings()
    {	
    	try {
			ConfigReader cr = new ConfigReader(CONFIGURATION_FILE);
			
			cr.put("language", languageChooser.getSelectedIndex());
			cr.put("gender", genderChooser.getSelectedIndex());
			cr.put("age", ageRangeChooser.getSelectedIndex());
			cr.put("dialect", dialectChooser.getSelectedIndex());
			cr.put("microphone", microphoneChooser.getSelectedIndex());
			//cr.put("username", usernameTextField.getText());
			System.out.println("username:" +  userName);
			cr.put("username", userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    
    /**
     *  Load all settings that were saved from the last session
     * 
     */
    public void loadSettings()
    {	
		ConfigReader cr=null;
		try {
			cr = new ConfigReader(CONFIGURATION_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			languageChooser.setSelectedIndex(
					cr.getInt("language",languageChooser.getSelectedIndex()));
		} catch (Exception e) { // to catch IndexOutOfBoundsException or any other exceptions
			languageChooser.setSelectedIndex(-1); // -1 mean no selection			
		}
		
		try {
			genderChooser.setSelectedIndex(
					cr.getInt("gender",genderChooser.getSelectedIndex()));
		} catch (Exception e) { // to catch IndexOutOfBoundsException or any other exceptions
			genderChooser.setSelectedIndex(-1); // -1 mean no selection			
		}
		
		try {		
			ageRangeChooser.setSelectedIndex(
					cr.getInt("age", ageRangeChooser.getSelectedIndex()));
		} catch (Exception e) {
			ageRangeChooser.setSelectedIndex(-1); 		
		}
		
		try {	
			dialectChooser.setSelectedIndex(  
					cr.getInt("dialect", dialectChooser.getSelectedIndex()));
		} catch (Exception e) {
			dialectChooser.setSelectedIndex(-1); 			
		}
		
		try {			
			microphoneChooser.setSelectedIndex(
					cr.getInt("microphone", microphoneChooser.getSelectedIndex()));
		} catch (Exception e) {
			microphoneChooser.setSelectedIndex(-1); 		
		}
		
		try {		
			userName = cr.getString("username", usernameTextField.getText());
			//usernameTextField.setText(
			//		cr.getString("username", usernameTextField.getText()));
			usernameTextField.setText(userName);
			
		} catch (Exception e) {
			usernameTextField.setText("");
		} 	
    }
	
    /**
     * convert user data to a String
     * 
     * @return
     */
    public String userDataToString () {
		String user = "";
		
		user = "User Name:" + userName + System.getProperty("line.separator");
		user = user + System.getProperty("line.separator");	

		user = user + "Speaker Characteristics:" + System.getProperty("line.separator");
		user = user + System.getProperty("line.separator");	
		user = user + "Gender: " + gender + System.getProperty("line.separator");
		user = user + "Age Range: " + ageRange + System.getProperty("line.separator"); 
		user = user + "Language: " + language + System.getProperty("line.separator");	
		user = user + "Pronunciation dialect: " + dialect + System.getProperty("line.separator");	
		user = user + System.getProperty("line.separator");
	
		user = user + "Recording Information:" + System.getProperty("line.separator");	
		user = user + System.getProperty("line.separator");
		user = user + "Microphone make: n/a" + System.getProperty("line.separator");	
		user = user + "Microphone type: " + microphone + System.getProperty("line.separator");	
		user = user + "Audio card make: unknown" + System.getProperty("line.separator");	
		user = user + "Audio card type: unknown" + System.getProperty("line.separator");
		user = user + "Audio Recording Software: VoxForge Speech Submission Application" + System.getProperty("line.separator");
		user = user + "O/S:" + System.getProperty("line.separator");	
		user = user + System.getProperty("line.separator");	
		
		user = user + "File Info:" + System.getProperty("line.separator");
		user = user + System.getProperty("line.separator");
		user = user + "File type: " + fileType + System.getProperty("line.separator");
		user = user + "Sampling Rate: " + samplingRate + System.getProperty("line.separator");
		user = user + "Sample rate format: " + samplingRateFormat + System.getProperty("line.separator");
		user = user + "Number of channels: " + numberChannels + System.getProperty("line.separator");	
	
		return user;
	}
    
    /**
     * returns a String with line breaks at maxLength
     * 
     * @param target
     * @param maxLength
     * @param currentLocale
     * @return
     */
    static String formatLines(
    	    String target, 
    	    int maxLength,
    	    Locale currentLocale) 
    {
    	String result;
    	StringBuffer sb = new StringBuffer();
    	
	    BreakIterator boundary = BreakIterator.
	        getLineInstance(currentLocale);
	    boundary.setText(target);
	    int start = boundary.first();
	    int end = boundary.next();
	    int lineLength = 0;

	    while (end != BreakIterator.DONE) {
	        String word = target.substring(start,end);
	        lineLength = lineLength + word.length();
	        if (lineLength >= maxLength) {
	        	sb.append(System.getProperty("line.separator"));
	            lineLength = word.length();
	        }
	        
	        sb.append(word);
	        start = end;
	        end = boundary.next();
	    }
	    
	    result = sb.toString();
	    
	    return result;
	}
    
} 
