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
import java.util.Calendar;

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

	// TODO need some way to create same session config file - by date and time???
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

    int numberofPrompts = 3;

    JButton [] playA = new JButton [numberofPrompts]; 
    JButton [] captA = new JButton [numberofPrompts];

    JButton uploadB;
    //JButton saveLocalB;
    JButton moreInfoB;    
    JButton aboutB; 

    boolean [] play_stateA = new boolean [numberofPrompts];
    boolean [] capt_stateA = new boolean [numberofPrompts];

    JTextField textField;

    String fileName = "untitled";
    String errStr;

    double [] durationA= new double [numberofPrompts];
    double duration = 0;

    double seconds;

    long [] totalBytesWrittenA= new long [numberofPrompts];
    long totalBytesWritten = 0L;
     
    File file;
  
    private File wavFile;
    private final File[] wavFileA = new File [numberofPrompts];
 
    private final File [] uploadWavFileA = new File [numberofPrompts];

    JProgressBar progBar;
    int sentBytes;
    int totalBytes;
    int buttonClicked;

    JTextField subjectBox;
    String subject;

    String [] promptA = new String [numberofPrompts];
    String [] promptidA = new String [numberofPrompts];
    
//  ############ Localized Fields ####################################   
    JTextField usernameTextField;  
//    String usernamePanelLabel;
    String userName;
//    String usernamePanelText;
    
//    String copyrightName;
//    String gplAccepted;
    
    String pleaseSelect;
//    String notApplicable;
    
//    String genderPanelLabel;
	JComboBox<String[]> genderChooser;       
//    String[] genderSelection;
    String gender;
    
//    String ageRangePanelLabel;
    JComboBox<String[]> ageRangeChooser; 
//    String[] ageSelection;
    String ageRange;

//    String dialectPanelLabel;
    JComboBox<String[]> dialectChooser;
//    String[] dialectSelection;
    String dialect;  
    
//    String microphonePanelLabel;
    JComboBox<String[]> microphoneChooser;     
//    String[] microphoneSelection;
    String microphone;  
    
//    String uploadText;
//    String uploadButtonLabel;
    
    String moreInfoText;
    String moreInfoButtonLabel;    

    String disclaimerText;
    String aboutButtonLabel;
    
    String recordButton; 
    String stopButton; 
    String playButton; 
    
    String uploadCompletedMessageLabel;
//  ############ Localized Fields ####################################   
    private Boolean leftToRight; // direction of text
    
    String targetDirectory;
    URL destinationURL;
    
    String language;
    String endpage;
    String helppage;
    String cookie;
    
    String licenseNotice;
    String vflicense;
  
	String tempdir;

	SaveOrUpload saveOrUpload; 
	//ConvertAndSavelocally convertAndSavelocally; 
	
    Color voxforgeColour 	= new Color(197, 216, 234);
    
    LabelLocalizer labels;
    
    // constructor
    public CapturePlayback(String lang, String targetDirectory, String destination) 
	{    	
		// ############ Localized Fields ####################################
		this.language = lang;
		this.targetDirectory = targetDirectory;
        try 
        {
        	this.destinationURL = new URL(destination);
        } 
        catch(java.net.MalformedURLException malurlex)
        {
            System.out.println( "Badly formed destination URL: " + destination);
        } 
        catch(java.lang.NullPointerException npe)
        {
            System.out.println( "destination is null" );
        }
		
		this.capturePlayback = this;
		
	    this.labels = new LabelLocalizer(this.language);
//	    usernamePanelLabel = labels.getUsernamePanelLabel();
//	    usernamePanelText = labels.getUsernamePanelText();
	    
//	    copyrightName = labels.getCopyrightName();
//	    gplAccepted = labels.getGplAccepted();

		Calendar cal = Calendar.getInstance();
		licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + labels.getCopyrightName() + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") 
				+ License.getBlanklicenseNotice();				
		vflicense = License.getVFLicense();	 	

        tempdir = getTempDir(); // creates new temp dir with every call
		saveOrUpload = new SaveOrUpload(
				capturePlayback,
				destinationURL, 
				labels.getUploadingMessageLabel(), 
				numberofPrompts,
				uploadWavFileA,
			    promptidA,
			    promptA,
			    tempdir,
			    licenseNotice,
			    BUFFER_SIZE,
			    language
		);
		//convertAndSavelocally = new ConvertAndSavelocally();
		
	    pleaseSelect = labels.getPleaseSelect();
//	    notApplicable = labels.getNotApplicable();
	    
//	    genderPanelLabel = labels.getGenderPanelLabel();
//	    genderSelection = labels.getGenderSelection();
	    gender = labels.getNotApplicable(); // default selection
	    
//	    ageRangePanelLabel = labels.getAgeRangePanelLabel();
//	    ageSelection = labels.getAgeSelection();
	    ageRange = labels.getNotApplicable(); // default selection
	
//	    dialectPanelLabel = labels.getDialectPanelLabel();
//	    dialectSelection = labels.getDialectSelection();
	    dialect = labels.getNotApplicable();  // default selection
	    
//	    microphonePanelLabel = labels.getMicrophonePanelLabel();
//	    microphoneSelection = labels.getMicrophoneSelection();
	    microphone = labels.getNotApplicable();  // default selection
	    
//	    uploadText = labels.getUploadText();
//	    uploadButtonLabel = labels.getUploadButtonLabel();
	    
	    moreInfoText = labels.getMoreInfoText();
	    moreInfoButtonLabel = labels.getMoreInfoButtonLabel();    
	
	    disclaimerText = labels.getDisclaimerText() ;
	    aboutButtonLabel = labels.getAboutButtonLabel();
	    
	    recordButton = labels.getRecordButton(); 
	    stopButton = labels.getStopButton(); 
	    playButton = labels.getPlayButton(); 
	    
	    uploadCompletedMessageLabel = labels.getUploadCompletedMessageLabel();
	    
	    leftToRight = labels.getLeftToRight();

	    playback = new Playback(
		    	capturePlayback,
		    	format,
        		numberofPrompts,
        		labels.getPeakWarningLabel(),
    	    	labels.getSampleGraphFileLabel(),
    	    	labels.getSampleGraphLengthLabel(), 
    	    	labels.getSampleGraphPositionLabel(),
                playButton,
                stopButton,
                bufSize
	    );
	    
	    capture = new Capture(
	    	capturePlayback,
	    	format,
    		labels.getPeakWarningLabel(),
	    	labels.getSampleGraphFileLabel(),
	    	labels.getSampleGraphLengthLabel(), 
	    	labels.getSampleGraphPositionLabel()
	    );
	    
		JPanel userPanel = startApp();
		JScrollPane scrollPane = new JScrollPane(userPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(300, 300));

		// Load all settings that were saved from the last session
        loadSettings();
	}

	// methods
	/**
	 * see http://stackoverflow.com/questions/14874613/how-to-replace-jpanel-with-another-jpanel
	 */
    private JPanel startApp() 
    { 	
		JPanel userPanel = new JPanel();

        addUserInfo(userPanel);
        addPromptInfo(userPanel, numberofPrompts);

	    createWavFiles(numberofPrompts, this.promptidA ); // promptidA array gets assigned in addPromptInfo
        addGraph(userPanel); 
        addRemainingPanelInfo(userPanel); 

        return userPanel;
    }	
    
	/**
	 * see http://stackoverflow.com/questions/14874613/how-to-replace-jpanel-with-another-jpanel
	 */
    private void restartApp() 
    { 	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		removeAll();  //Removes all the components from this container
        tempdir = getTempDir(); // creates new temp dir with every call
        
		JPanel userPanel = startApp();
        //add(userPanel);  
		JScrollPane scrollPane = new JScrollPane(userPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(scrollPane, BorderLayout.CENTER);
		setPreferredSize(new Dimension(300, 300));
        
        loadSettings();
        
        validate();
        repaint();
        setVisible(true);
    }	

	/**
	 * add prompts to GUI
	 * 
	 * @param userPanel
	 * @param numberofPrompts
	 */
    private void addPromptInfo(JPanel userPanel, int numberofPrompts) 
    { 
    	JPanel promptsContainer = new JPanel();
	    String [][] promptArray = (new Prompts(numberofPrompts,this.language)).getPrompts();
	    for (int i = 0; i < numberofPrompts; i++) 
	    {
	    	this.promptidA [i] = promptArray[0][i];
	    	this.promptA [i] = promptArray[1][i];
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
	        promptInnerPanelA[i].add(new MultiLineLabel(promptPanelA[i], this.promptA[i], maxWidth, leftToRight));     
	        promptPanelA[i].add(promptInnerPanelA[i]);
	        playA[i] = addButton(playButton, promptPanelA[i], false);
	        if (i==0) {
	        	captA[i] = addButton(recordButton, promptPanelA[i], true); // only turn on first record button 
	        } else {
		        captA[i] = addButton(recordButton, promptPanelA[i], false);
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
	        	wavFileA [i] = new File(tempdir + "wavFile" + i + ".wav");
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
			System.err.println("CapturePlayback's WAV file for recording uploadWavFile" + i + "is:" + uploadWavFileA[i]);
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
        				playback
        );
        samplingGraph.setPreferredSize(new Dimension(50, 100));
        samplingPanel.add(samplingGraph);
        
        userPanel.add(samplingPanel);
        
    	//		############ Upload Progress bar ####################################
        progBar = new JProgressBar();
        progBar.setStringPainted(true);
        progBar.setString("Ready to Record");
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
        uploadTextPanel.add(new JLabel(labels.getUploadText()));               
        p2.add(uploadTextPanel);
	//		############ Upload ####################################          
        JPanel uploadButtonPanel = new JPanel();
        uploadButtonPanel.setBorder(new EmptyBorder(5,0,5,0));
        uploadB = addButton(labels.getUploadButtonLabel(), uploadButtonPanel, false); // upload all submissions
        p2.add(uploadButtonPanel);
    	//		############ Save Local ####################################          
        //saveLocalB = addButton("save on your computer", uploadButtonPanel, false); // upload all submissions
        //p2.add(uploadButtonPanel);
           
	// 		############ More Information Button ####################################          
        JPanel moreInfoButtonPanel = new JPanel();
        if (leftToRight)
        {
	        moreInfoButtonPanel.add(new JLabel(moreInfoText));
	        moreInfoB = addButton(moreInfoButtonLabel, moreInfoButtonPanel, true); 
        }
        else
        {
       	    moreInfoB = addButton(moreInfoButtonLabel, moreInfoButtonPanel, true); 
            moreInfoButtonPanel.add(new JLabel(moreInfoText));     	 
        }
        p2.add(moreInfoButtonPanel);   
// 		############ Disclaimer ####################################  
        JPanel DisclaimerPanel = new JPanel();
        DisclaimerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JPanel DisclaimerInnerPanel = new JPanel(); 
        if (leftToRight)
        {
        	//DisclaimerInnerPanel.add(new JLabel(disclaimerText));
        	aboutB = addButton(aboutButtonLabel, DisclaimerInnerPanel, true); 
        }
        else
        {
            aboutB = addButton(aboutButtonLabel, DisclaimerInnerPanel, true); 
            DisclaimerInnerPanel.add(new JLabel(disclaimerText));
        }
        DisclaimerInnerPanel.setBorder(BorderFactory.createLineBorder (voxforgeColour, 3));
        DisclaimerPanel.add(DisclaimerInnerPanel);        
        p2.add(DisclaimerPanel); 
	//#########################################################################   
    }
	
    /**
     * User name,
     * Gender,
     * Age Range,
     * Pronunciation Dialect,
     * Microphone Type
     **/
    private void addUserInfo(JPanel p2) 
    { 
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
	// 		############ User name ####################################             
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        if (leftToRight)
        {
	        usernamePanel.add(new JLabel(labels.getUsernamePanelLabel()));
	        usernamePanel.add(usernameTextField = new JTextField(20));
        }
        else
        {
            usernamePanel.add(usernameTextField = new JTextField(20));        	
            usernamePanel.add(new JLabel(labels.getUsernamePanelLabel()));        	
        }
        usernamePanel.add(new JLabel(labels.getUsernamePanelText()));     
        p2.add(usernamePanel);  
    // 		############ Gender ####################################             
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  
        if (leftToRight)
        {
	        genderPanel.add(new JLabel(labels.getGenderPanelLabel()));
	        genderPanel.add(genderChooser = new JComboBox(labels.getGenderSelection()));
        }
        else
        {
            genderPanel.add(genderChooser = new JComboBox(labels.getGenderSelection()));   
        	genderPanel.add(new JLabel(labels.getGenderPanelLabel()));
    	
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
        if (leftToRight)
        {
	        ageRangePanel.add(new JLabel(labels.getAgeRangePanelLabel()));
			ageRangePanel.add(ageRangeChooser = new JComboBox(labels.getAgeSelection()));
        }
        else
        {
    		ageRangePanel.add(ageRangeChooser = new JComboBox(labels.getAgeSelection()));       	
            ageRangePanel.add(new JLabel(labels.getAgeRangePanelLabel()));
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
        if (leftToRight)
        {
        	dialectPanel.add(new JLabel(labels.getDialectPanelLabel()));
        	dialectPanel.add(dialectChooser = new JComboBox(labels.getDialectSelection()));
        }
        else
        {
        	dialectPanel.add(dialectChooser = new JComboBox(labels.getDialectSelection()));       
        	dialectPanel.add(new JLabel(labels.getDialectPanelLabel()));
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
        if (leftToRight)
        {
	        microphonePanel.add(new JLabel(labels.getMicrophonePanelLabel()));
	        microphonePanel.add(microphoneChooser = new JComboBox(labels.getMicrophoneSelection()));
        }
        else
        {
            microphonePanel.add(microphoneChooser = new JComboBox(labels.getMicrophoneSelection()));
            microphonePanel.add(new JLabel(labels.getMicrophonePanelLabel()));            
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
     * create temporary directory and return path as a string
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
            for (int i = 0; i < numberofPrompts; i++) {
            	playA[i].doClick(0);
            }
        }
        if (capture.thread != null) {
            for (int i = 0; i < numberofPrompts; i++) {
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
        for (int i = 0; i < numberofPrompts; i++) {
        	playA[i].setEnabled(false); 
        	captA[i].setEnabled(false); 
        }
    }
    
    private void saveButtonState() {
        for (int i = 0; i < numberofPrompts; i++) {
        	if (playA[i].isEnabled()) {play_stateA [i] = true;} else {play_stateA [i] = false;}
        	if (captA[i].isEnabled()) {capt_stateA [i] = true;} else {capt_stateA [i] = false;}
        }
    }
    
    protected void restoreButtonState() {
        for (int i = 0; i < numberofPrompts; i++) {
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
        for (int i = 0; i < numberofPrompts; i++) {
            if (obj.equals(playA[i])) {
                if (playA[i].getText().startsWith(playButton)) {
                    wavFile = wavFileA[i];      
                    duration = durationA[i];
                    totalBytesWritten = totalBytesWrittenA[i];
                    System.err.println("=== Play " + (i+1) + " ===");

                    fileName = promptidA[i];  
                    playback.start(
                    		audioInputStream,
	                		samplingGraph,
	                		progBar,
	                        playA, 
	                        captA,
	                        promptidA[i],
	                        duration
                    );
            		System.err.println("duration:" + duration);

                    samplingGraph.start();
	                saveButtonState(); 
	                setButtonsOff();
                    captA[i].setEnabled(false);
                    playA[i].setEnabled(true);
                    playA[i].setText(stopButton);
                } else {
                    playback.stop();
                    samplingGraph.stop();
	                restoreButtonState(); 
                    captA[i].setEnabled(true);
                    playA[i].setText(playButton);
                }
            }
        }

// ################### Record (capture) #######################################        
	    for (int x = 0; x < numberofPrompts; x++) {
	        if (obj.equals(captA[x])) {
	            if (captA[x].getText().startsWith(recordButton)) {
	                file = null;
	                wavFile = wavFileA[x];  
	                fileName = promptidA[x];
	        		System.err.println("=== Record " + (x+1) + " ==="); 
	                capture.start(
	                		samplingGraph,
	                		audioInputStream, 
	                		progBar,
	                		uploadWavFileA[x],
	                		wavFileA[x],
	                		promptidA[x]
	                );  
	                samplingGraph.start();
	                saveButtonState(); 
	                setButtonsOff(); 
	                captA[x].setEnabled(true);    
	                captA[x].setText(stopButton);
	                moreInfoB.setEnabled(false);  
	                aboutB.setEnabled(false); 
	            } else {
	            	samplingGraph.removeAllLinesElements();
	                try {  
	                	Thread.sleep(1000);
	                } catch (InterruptedException ex) { 
	        			System.err.println("Recording Thread - Interrupt Exception");
	                }
	                
	                CaptureResult result = capture.stop();
	                audioInputStream = result.audioInputStream;
	                duration = result.duration;
	                totalBytesWritten = result.totalBytesWritten;
	                
	                totalBytesWrittenA[x] = totalBytesWritten; 
	            	durationA[x]= totalBytesWritten / (double) (format.getSampleRate() * format.getSampleSizeInBits()/ 8);
	        		System.err.println("duration1:" + durationA[x]);
	            	samplingGraph.stop();
	                restoreButtonState(); 
	                playA[x].setEnabled(true);
	                captA[x].setText(recordButton);
	                moreInfoB.setEnabled(true);  
	                aboutB.setEnabled(true); 
	                captA[x].setEnabled(true);
	                if (x < numberofPrompts-1) {
	                	captA[x+1].setEnabled(true);
	                }
	        		System.err.println("x " + x +"numberofPrompts " +numberofPrompts);
	                if (x == numberofPrompts-1) {
	                	uploadB.setEnabled(true);
	                	//saveLocalB.setEnabled(true);
	                }
	            }
	        } 
	    }

//          ################### Upload #######################################               
	    if (obj.equals(uploadB)) 
	    { 
	        for (int i = 0; i < numberofPrompts; i++) 
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

			saveSettings();

			totalBytes = saveOrUpload.start(progBar, userName, userDataToString() );
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
    	AudioInputStream audioInputStream;
    	double duration;
    	long totalBytesWritten;
    }
    
    /**
	 * Will free audio input stream if exists, and grab a new version. This seems a bit silly but there's no other way to "rewind" large files.
	 * @uml.property  name="audioInputStream"
	 */
    protected AudioInputStream getAudioInputStream(){
		if (audioInputStream != null) {
			try {
				audioInputStream.close();
			} catch (IOException err) {
			}
		}
		System.err.println("getAudioInputStream - totalBytesWritten:" + totalBytesWritten);
		try {
			audioInputStream = new AudioInputStream(new BufferedInputStream(
					new FileInputStream(wavFile)), format, totalBytesWritten
					/ (format.getChannels() * format.getSampleSizeInBits() / 8) // Length in sample frames
			);

 			
		} catch (Exception err) {
			System.err.println("Exception while reading cache file: " + err);
		}
		System.err.println("Grabbed audio input stream from cache file");
		
		return audioInputStream;
	}

    /**
     * always called by Postlet uploader
     */
 	public synchronized void setProgress(int a) {
         sentBytes += a;
         progBar.setValue(sentBytes);
         if (sentBytes == totalBytes)
         {
            progBar.setStringPainted(true);
           	progBar.setString(uploadCompletedMessageLabel);
            progBar.setIndeterminate(false);
            System.err.println("Finished! submission uploaded to VoxForge repository");
            // Reset the applet
            progBar.setValue(0);
         } 
         else 
         {
         	//FORDEBUG
         	// System.err.println("setProgress(): Not reached end yet. sentBytes="+sentBytes+", totalBytes="+totalBytes);
         }
 	}    
    
    
    public void saveSettings()
    {	
    	try {
			ConfigReader cr = new ConfigReader(CONFIGURATION_FILE);

			cr.put("gender", genderChooser.getSelectedIndex());
			cr.put("age", ageRangeChooser.getSelectedIndex());
			cr.put("dialect", dialectChooser.getSelectedIndex());
			cr.put("microphone", microphoneChooser.getSelectedIndex());
			cr.put("username", usernameTextField.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
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
			usernameTextField.setText(
					cr.getString("username", usernameTextField.getText()));
		} catch (Exception e) {
			usernameTextField.setText("");
		} 	
    }
	
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
} 
