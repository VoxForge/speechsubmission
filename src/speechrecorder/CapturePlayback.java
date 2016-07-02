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


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
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

import net.sf.postlet.UploadManager;
import netscape.javascript.JSObject;
import speechrecorder.ConfigReader;

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

    Capture capture = new Capture();
    Playback playback = new Playback();
    // !!!!!!
    //ConvertAndUpload convertAndUpload = new ConvertAndUpload();
    // !!!!!!   
    CapturePlayback capturePlayback; // Needed for referencing within the inner classes

    AudioInputStream audioInputStream;
    SamplingGraph samplingGraph;

    int numberofPrompts = 10;
    JButton [] playA = new JButton [numberofPrompts]; //creates the array, not the objects!
    JButton [] captA = new JButton [numberofPrompts]; //creates the array, not the objects!
    
    JButton uploadB;
    JButton moreInfoB;    
    JButton aboutB; 

    boolean [] play_stateA = new boolean [numberofPrompts];
    boolean [] capt_stateA = new boolean [numberofPrompts];

    // !!!!!!
    //License licenseObject = new License();
    // String license = licenseObject.getLicense();
    // String VFlicense = licenseObject.getVFLicense();
    // !!!!!!
    JTextField textField;

    String fileName = "untitled";
    String errStr;

    double [] durationA= new double [numberofPrompts];
    double duration = 0;

    double seconds;

    long [] totalBytesWrittenA= new long [numberofPrompts];
    long totalBytesWritten = 0L;
     
    File file;
	Vector<Double> lines = new Vector<Double>();
  
    private File wavFile;
    private final File[] wavFileA = new File [numberofPrompts];
 
    private final File [] uploadWavFileA = new File [numberofPrompts];
  
    private File promptsFile;  
    private File readmeFile;    
    private File licenseFile;   
    private File licenseNoticeFile;    

    JProgressBar progBar;
    int sentBytes;
    int totalBytes;
    int buttonClicked;

    JTextField subjectBox;
    String subject;

    String [] promptA = new String [numberofPrompts];
    String [] promptidA = new String [numberofPrompts];
 
//  required for the PHP uploader to work properly
    String fileFieldName = "userfile"; 
    
//  ############ Localized Fields ####################################   
    JTextField usernameTextField;  
    String usernamePanelLabel;
    String userName;
    String usernamePanelText;
    
    String copyrightName;
    String gplAccepted;
    
    String pleaseSelect;
    String notApplicable;
    
    String genderPanelLabel;
	JComboBox<String[]> genderChooser;       
    String[] genderSelection;
    String gender;
    
    String ageRangePanelLabel;
    JComboBox<String[]> ageRangeChooser; 
    String[] ageSelection;
    String ageRange;

    String dialectPanelLabel;
    JComboBox<String[]> dialectChooser;
    String[] dialectSelection;
    String dialect;  
    
    String microphonePanelLabel;
    JComboBox<String[]> microphoneChooser;     
    String[] microphoneSelection;
    String microphone;  
    
    String uploadText;
    String uploadButtonLabel;
    
    String moreInfoText;
    String moreInfoButtonLabel;    

    String disclaimerText;
    String aboutButtonLabel;
    
    String recordButton; 
    String stopButton; 
    String playButton; 
    
    String peakWarningLabel; 
    String sampleGraphFileLabel; 
    String sampleGraphLengthLabel; 
    String sampleGraphPositionLabel; 
    String uploadingMessageLabel;
    String uploadCompletedMessageLabel;
//  ############ Localized Fields ####################################   
    private Boolean leftToRight; // direction of text
    
    URL endPageURL;
    URL helpPageURL;
    URL destinationURL;
    String language;
    String endpage;
    String helppage;
    String cookie;
    
    String licenseNotice;
    String vflicense;
  
	String tempdir = getTempDir();

	// !!!!!!
	ConvertAndUpload convertAndUpload; 
	// !!!!!!
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CapturePlayback(String lang, URL destinationURL, 
	        URL endPageURL, URL helpPageURL, String cookie) {    	
	//  ############ Localized Fields ####################################
		// !!!!!!
		this.language = lang;
		//this.language = "FA"; // for testing 
		// !!!!!!
		
	    LabelLocalizer labels = new LabelLocalizer(this.language);
	    usernamePanelLabel = labels.getUsernamePanelLabel();
	    usernamePanelText = labels.getUsernamePanelText();
	    
	    copyrightName = labels.getCopyrightName();
	    gplAccepted = labels.getGplAccepted();

		// !!!!!!
		Calendar cal = Calendar.getInstance();
		//if (this.language.equals("EN") )
		//{
		//	licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + copyrightName + System.getProperty("line.separator") 
		//			+ System.getProperty("line.separator") 
		//			+ License.getEN_BlanklicenseNotice();
		//	vflicense = License.getEN_VFLicense();
		//	//System.err.println("!!!!!! EN licenseNotice:[" + licenseNotice + "] !!!!!!"); // debug
		//}
		//else
		//{
		licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + copyrightName + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") 
				+ License.getBlanklicenseNotice();				
		vflicense = License.getVFLicense();	 		
		//.err.println("!!!!!! NOT-EN licenseNotice:[" + licenseNotice + "] !!!!!!"); // debug
		//}
		//System.err.println("!!!!!! CapturePlayback Language:[" + this.language + "] !!!!!!"); // debug
		convertAndUpload = new ConvertAndUpload();
		// !!!!!!	    
	    
	    pleaseSelect = labels.getPleaseSelect();
	    notApplicable = labels.getNotApplicable();
	    
	    genderPanelLabel = labels.getGenderPanelLabel();
	    genderSelection = labels.getGenderSelection();
	    gender = notApplicable; // default selection
	    
	    ageRangePanelLabel = labels.getAgeRangePanelLabel();
	    ageSelection = labels.getAgeSelection();
	    ageRange = notApplicable; // default selection
	
	    dialectPanelLabel = labels.getDialectPanelLabel();
	    dialectSelection = labels.getDialectSelection();
	    dialect = notApplicable;  // default selection
	    
	    microphonePanelLabel = labels.getMicrophonePanelLabel();
	    microphoneSelection = labels.getMicrophoneSelection();
	    microphone = notApplicable;  // default selection
	    
	    uploadText = labels.getUploadText();
	    uploadButtonLabel = labels.getUploadButtonLabel();
	    
	    moreInfoText = labels.getMoreInfoText();
	    moreInfoButtonLabel = labels.getMoreInfoButtonLabel();    
	
	    disclaimerText = labels.getDisclaimerText() ;
	    aboutButtonLabel = labels.getAboutButtonLabel();
	    
	    recordButton = labels.getRecordButton(); 
	    stopButton = labels.getStopButton(); 
	    playButton = labels.getPlayButton(); 
	    
	    peakWarningLabel = labels.getPeakWarningLabel(); 
	    sampleGraphFileLabel = labels.getSampleGraphFileLabel(); 
	    sampleGraphLengthLabel = labels.getSampleGraphLengthLabel(); 
	    sampleGraphPositionLabel = labels.getSampleGraphPositionLabel(); 
	    
	    uploadingMessageLabel = labels.getUploadingMessageLabel();
	    uploadCompletedMessageLabel = labels.getUploadCompletedMessageLabel();
	    
	    leftToRight = labels.getLeftToRight();
	    
	//  ############ Localized Fields ####################################  
	
		String [][] promptArray = (new Prompts(numberofPrompts,this.language)).getPrompts();
	    for (int i = 0; i < numberofPrompts; i++) {
	    	this.promptidA [i] = promptArray[0][i];
	    	this.promptA [i] = promptArray[1][i];
	    	// System.err.println("Prompts:" + this.promptidA[i] + ":"+ this.promptA [i]);
	    }
	   
	    this.destinationURL = destinationURL;
	    this.endPageURL = endPageURL;
	    this.helpPageURL = helpPageURL;
	    this.cookie = cookie;
	    
	    capturePlayback = this;
	
		// Create WAV files to hold recordings
		try {
	        for (int i = 0; i < numberofPrompts; i++) {
	        	wavFileA [i] = new File(tempdir + "wavFile" + i + ".wav");
	        	wavFileA[i].deleteOnExit();
	        }
	        for (int i = 0; i < numberofPrompts; i++) {
				uploadWavFileA[i] = new File(tempdir + this.promptidA [i] + ".wav");
				uploadWavFileA[i].deleteOnExit();
	        }
			promptsFile = new File(tempdir + "prompts.txt");			
			promptsFile.deleteOnExit();		
			readmeFile = new File(tempdir + "readme.txt");			
			readmeFile.deleteOnExit();		
			licenseFile = new File(tempdir + "GPL_license.txt");			
			licenseFile.deleteOnExit();			
			licenseNoticeFile = new File(tempdir + "license.txt");	
			licenseNoticeFile.deleteOnExit();	
		} catch (Exception e) {
			System.err.println("Unable to create WAV cache file for storing audio\n" + e);
			return;
		}
	    for (int i = 0; i < numberofPrompts; i++) {			
			System.err.println("CapturePlayback's WAV file for recording uploadWavFile" + i + "is:" + uploadWavFileA[i]);
	    }
	
	//  ############ GUI Display ####################################   
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	        EmptyBorder eb = new EmptyBorder(5,5,5,5);
	        SoftBevelBorder sbb = new SoftBevelBorder(SoftBevelBorder.LOWERED);
	
	        JPanel p2 = new JPanel();
	        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
	//      ############ User name ####################################             
	//      userName is read when user clicks Upload
	        JPanel usernamePanel = new JPanel();
	        usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
	        if (leftToRight)
	        {
		        usernamePanel.add(new JLabel(usernamePanelLabel));
		        usernamePanel.add(usernameTextField = new JTextField(20));
	        }
	        else
	        {
	            usernamePanel.add(usernameTextField = new JTextField(20));        	
	            usernamePanel.add(new JLabel(usernamePanelLabel));        	
	        }
	        usernamePanel.add(new JLabel(usernamePanelText));     
	        p2.add(usernamePanel);  
	// ############ Gender ####################################             
	        JPanel genderPanel = new JPanel();
	        genderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  
	        if (leftToRight)
	        {
		        genderPanel.add(new JLabel(genderPanelLabel));
		        genderPanel.add(genderChooser = new JComboBox(genderSelection));
	        }
	        else
	        {
	            genderPanel.add(genderChooser = new JComboBox(genderSelection));   
	        	genderPanel.add(new JLabel(genderPanelLabel));
	    	
	        }
	        genderChooser.setSelectedIndex(0);       
	        genderChooser.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
	                     gender = (String)genderChooser.getSelectedItem();
	                 }
	        	});
	        p2.add(genderPanel);
	// ############ Age Range ####################################             
	        JPanel ageRangePanel = new JPanel();
	        ageRangePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
	        if (leftToRight)
	        {
		        ageRangePanel.add(new JLabel(ageRangePanelLabel));
				ageRangePanel.add(ageRangeChooser = new JComboBox(ageSelection));
	        }
	        else
	        {
	    		ageRangePanel.add(ageRangeChooser = new JComboBox(ageSelection));       	
	            ageRangePanel.add(new JLabel(ageRangePanelLabel));
	        }
	        ageRangeChooser.setSelectedIndex(0);          
	        ageRangeChooser.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
	                     ageRange = (String)ageRangeChooser.getSelectedItem();
	                 }
	        	});
	        p2.add(ageRangePanel);
	//      ############ Pronunciation Dialect: ####################################       
	        JPanel dialectPanel = new JPanel();
	        dialectPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	        if (leftToRight)
	        {
	        	dialectPanel.add(new JLabel(dialectPanelLabel));
	        	dialectPanel.add(dialectChooser = new JComboBox(dialectSelection));
	        }
	        else
	        {
	        	dialectPanel.add(dialectChooser = new JComboBox(dialectSelection));       
	        	dialectPanel.add(new JLabel(dialectPanelLabel));
	        }
	        dialectChooser.setSelectedIndex(0);  
	        dialectChooser.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
		                dialect = (String)dialectChooser.getSelectedItem();
		            }
	            });
	        p2.add(dialectPanel);
	//      ############ Microphone Type: ####################################       
	        JPanel microphonePanel = new JPanel();
	        microphonePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	        if (leftToRight)
	        {
		        microphonePanel.add(new JLabel(microphonePanelLabel));
		        microphonePanel.add(microphoneChooser = new JComboBox(microphoneSelection));
	        }
	        else
	        {
	            microphonePanel.add(microphoneChooser = new JComboBox(microphoneSelection));
	            microphonePanel.add(new JLabel(microphonePanelLabel));            
	        }
	        microphoneChooser.setSelectedIndex(0);  
	 //       microphoneChooser.setEditable(true); // user can add whatever they want ...
	        microphoneChooser.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
	                     microphone = (String)microphoneChooser.getSelectedItem();
	                 }
	        	});
	        p2.add(microphonePanel);
	//		############ Prompt container ####################################   
	        JPanel promptsContainer = new JPanel(); 
	        promptsContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
	        Color voxforgeColour 	= new Color(197, 216, 234);
	         int startPromptCount = 0;
	        int promptsPerPane = numberofPrompts;
	
	//      ############ Prompts panel ####################################         
	        JPanel prompts = new JPanel(); 
	        prompts.setLayout(new BoxLayout(prompts, BoxLayout.Y_AXIS));
	        prompts.setBorder(BorderFactory.createLineBorder (voxforgeColour, 3));
	    
	        int maxWidth = 40;
	
	        JPanel promptPanelA[] = new JPanel[promptsPerPane]; 
	        JPanel promptInnerPanelA[] = new JPanel[promptsPerPane]; 
	        for (int i = startPromptCount; i < promptsPerPane; i++) {
	        	promptPanelA[i] = new JPanel();
	        	//promptPanelA[i].setLayout(new FlowLayout(FlowLayout.LEFT));    
	        	promptPanelA[i].setLayout(new FlowLayout(FlowLayout.RIGHT));            	
	        	promptInnerPanelA [i]= new JPanel(); 
		        promptInnerPanelA[i].setBorder(BorderFactory.createLineBorder (voxforgeColour, 1));
		        //promptInnerPanelA[i].add(new MultiLineLabel(promptPanelA[i], this.promptA[i], maxWidth));
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
	        p2.add(promptsContainer);
	
	//      ############ Sampling Graph ####################################          
	        JPanel samplingPanel = new JPanel(new BorderLayout());
	        eb = new EmptyBorder(10,20,5,20);
	        samplingPanel.setBorder(new CompoundBorder(eb, sbb));
	        samplingPanel.add(samplingGraph = new SamplingGraph());
	        p2.add(samplingPanel);
	//      ############ Upload Text ####################################             
	        JPanel uploadTextPanel = new JPanel();
	        uploadTextPanel.add(new JLabel(uploadText));               
	        p2.add(uploadTextPanel);
	//		############ Upload ####################################          
	         JPanel uploadButtonPanel = new JPanel();
	         uploadButtonPanel.setBorder(new EmptyBorder(5,0,5,0));
	         uploadB = addButton(uploadButtonLabel, uploadButtonPanel, false); // upload all submissions
	         p2.add(uploadButtonPanel);
	//		############ Upload Progress bar ####################################
	         progBar = new JProgressBar();
	         progBar.setStringPainted(false);
	         progBar.setString("Ready");
	         //progBar.setVisible(false);
	         p2.add(progBar);               
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
	        	 DisclaimerInnerPanel.add(new JLabel(disclaimerText));
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
	         add(p2);
	   
	
	         // Load all settings that were saved from the last session
	         loadSettings();
	    }

    public void open() { }

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
            //System.err.println("setButtonsOff" + "playA" + i + playA[i].isEnabled()+ ";captA" + i + captA[i].isEnabled());// !!!!!!
        }
    }
    
    private void saveButtonState() {
        for (int i = 0; i < numberofPrompts; i++) {
       		// System.err.println("playA[i]" +  i ); // !!!!!!
        	if (playA[i].isEnabled()) {play_stateA [i] = true;} else {play_stateA [i] = false;}
        	if (captA[i].isEnabled()) {capt_stateA [i] = true;} else {capt_stateA [i] = false;}
            //System.err.println("saveButtonState" + "playA" + i + playA[i].isEnabled()+ ";captA" + i + captA[i].isEnabled());// !!!!!!
        }
    }
    
    private void restoreButtonState() {
        for (int i = 0; i < numberofPrompts; i++) {
         	if (play_stateA[i]) {playA[i].setEnabled(true);} else {playA[i].setEnabled(false);}
        	if (capt_stateA[i]) {captA[i].setEnabled(true);} else {captA[i].setEnabled(false);}
            // System.err.println("restoreButtonState" + "playA" + i + playA[i].isEnabled()+ ";captA" + i + captA[i].isEnabled());// !!!!!!
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
         
// ################### Play #######################################       
        for (int i = 0; i < numberofPrompts; i++) {
       		//System.err.println("playA[i]" +  i ); // !!!!!!
            if (obj.equals(playA[i])) {
                if (playA[i].getText().startsWith(playButton)) {
                    wavFile = wavFileA[i];      
                    duration = durationA[i];
                    totalBytesWritten = totalBytesWrittenA[i];
                    System.err.println("=== Play " + (i+1) + " ===");// !!!!!!
                    playback.start();
            		System.err.println("duration:" + duration);
                    fileName = promptidA[i];  
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
  
	    for (int x = 0; x < numberofPrompts; x++) {
	        if (obj.equals(captA[x])) {
	            if (captA[x].getText().startsWith(recordButton)) {
	                file = null;
	                wavFile = wavFileA[x];  
	        		System.err.println("=== Record " + (x+1) + " ==="); // !!!!!!
	                capture.start(uploadWavFileA[x]);  
	                fileName = promptidA[x];
	                samplingGraph.start();
	                saveButtonState(); 
	                setButtonsOff(); 
	                captA[x].setEnabled(true);    
	                captA[x].setText(stopButton);
	                moreInfoB.setEnabled(false);  
	                aboutB.setEnabled(false); 
	            } else {
	                lines.removeAllElements();  
	                try {  
	                	//capture.thread.sleep(1000);
	                	Thread.sleep(1000);
	                } catch (InterruptedException ex) { 
	        			System.err.println("Recording Thread - Interrupt Exception");
	                }
	                capture.stop();
	                totalBytesWrittenA[x] = totalBytesWritten; // !!!!!!
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
	                if (x == numberofPrompts-1) {
	                	uploadB.setEnabled(true);
	                }
	            }
	        } 
	    }

//          ################### Upload #######################################               
	    if (obj.equals(uploadB)) { 
	        for (int i = 0; i < numberofPrompts; i++) {
	        	playA[i].setEnabled(false);
	            captA[i].setEnabled(false);
	        }
               uploadB.setEnabled(false);               
        	try {
 	    	   usernameTextField.selectAll();
	    	   userName = usernameTextField.getText();
    		   // see   java.util.regex.Pattern
    		   //	   \w  	A word character: [a-zA-Z_0-9]
    		   //	   \W  	A non-word character: [^\w]
			   userName = (usernameTextField.getText().replaceAll("\\W",""));
	    	   if (userName.length() == 0 ) {
	               userName = "anonymous";
	    	   } else {
				   if (userName.length() > 40 ) {
					   userName = userName.substring(0,40);
	    		   } 
	    	   }
        	} catch (NullPointerException ex) { 
               userName = "anonymous";
            }

					saveSettings();
        	
           convertAndUpload.start();
        }
//      ################### More Information #######################################     
        else if (obj.equals(moreInfoB)) {
        	 // !!!!!!
             //JTextArea textArea = new JTextArea(license);
         	 JTextArea textArea = new JTextArea(License.getLicense());
             // !!!!!!
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
            // !!!!!!
            //JTextArea textArea = new JTextArea(VFlicense);
        	JTextArea textArea = new JTextArea(vflicense);
            // !!!!!!
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

    public void createAudioInputStream(File file, boolean updateComponents) {
        if (file != null && file.isFile()) {
            try {
                this.file = file;
                errStr = null;
                audioInputStream = AudioSystem.getAudioInputStream(file);
                fileName = file.getName();
                long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;
        		System.err.println("createAudioInputStream duration:" + duration);
                if (updateComponents) {
//DEL                    formatControls.setFormat(audioInputStream.getFormat());
                    samplingGraph.createWaveForm(null);
                }
            } catch (Exception ex) { 
                reportStatus(ex.toString());
            }
        } else {
            reportStatus("Audio file required.");
        }
    }


   public void saveToFile(String name, AudioFileFormat.Type fileType) {

        if (audioInputStream == null) {
            reportStatus("No loaded audio to save");
            return;
        } else if (file != null) {
            createAudioInputStream(file, false);
        }

        // reset to the beginning of the captured data
        try {
            audioInputStream.reset();
        } catch (Exception e) { 
            reportStatus("Unable to reset stream " + e);
            return;
        }

        File file = new File(fileName = name);
        try {
            if (AudioSystem.write(audioInputStream, fileType, file) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) { reportStatus(ex.toString()); }
        samplingGraph.repaint();
    }
   
   public void saveToFile(File file, AudioFileFormat.Type fileType) {

       if (audioInputStream == null) {
           reportStatus("No loaded audio to save");
           return;
       } else if (file != null) {
           createAudioInputStream(file, false);
       }

       // reset to the beginning of the captured data
       try {
           audioInputStream.reset();
       } catch (Exception e) { 
           reportStatus("Unable to reset stream " + e);
           return;
       }

 //      File file = new File(fileName = name);
       try {
           if (AudioSystem.write(audioInputStream, fileType, file) == -1) {
               throw new IOException("Problems writing to file");
           }
       } catch (Exception ex) { reportStatus(ex.toString()); }
       samplingGraph.repaint();
   }
   
    private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
            samplingGraph.repaint();
        }
    }


    /**
     * Write data to the OutputChannel.
     */
    public class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }
      
        public void stop() {
            thread = null;
        }
        
        private void shutDown(String message) {
            if ((errStr = message) != null) {
                System.err.println(errStr);
                samplingGraph.repaint();
            }
            if (thread != null) {
                thread = null;
                samplingGraph.stop();
    	        for (int i = 0; i < numberofPrompts; i++) {
    	        	if (playA[i].getText().startsWith(stopButton)) { //play button gets set to "Stop" after play is pressed
    	        		captA[i].setEnabled(true); 
    	        	}
    	        }
    	      for (int i = 0; i < numberofPrompts; i++) {
                  playA[i].setText(playButton);
    	      }
            } 
        }

		public void run() {
			getAudioInputStream();

            // get an AudioInputStream of the desired format for playback
//DEL            AudioFormat format = formatControls.getFormat();
            AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
                        
            if (playbackInputStream == null) {
                shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
                return;
            }

            // define the required attributes for our line, 
            // and make sure a compatible line is supported.
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, 
                format);
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the source data line for playback.
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, bufSize);
            } catch (LineUnavailableException ex) { 
                shutDown("Unable to open the line: " + ex);
                return;
            }

            // play back the captured audio data
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;

            // start the source data line
            line.start();
            ByteArrayOutputStream outbaos = new ByteArrayOutputStream();
            while (thread != null) {
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
					outbaos.write(data, 0, numBytesRead);
		        	/// System.err.println("numBytesRead" + numBytesRead);
                } catch (Exception e) {
                    shutDown("Error during playback: " + e);
                    break;
                }
            }       
            byte audioBytes[] = outbaos.toByteArray();
        	// debug System.err.println("outbaos size:" + outbaos.size());            
        	outbaos.reset();
        	outbaos = null;
            samplingGraph.createWaveForm(audioBytes); 
            samplingGraph.repaint();
            
			getAudioInputStream();
            playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            
            progBar.setStringPainted(true);
            progBar.setString(samplingGraph.peakWarning ? 
            		peakWarningLabel : "");
            while (thread != null) {
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
                    int numBytesRemaining = numBytesRead;
                    while (numBytesRemaining > 0 ) {
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                    }
                } catch (Exception e) {
                    shutDown("Error during playback: " + e);
                    break;
                }
            }
            // we reached the end of the stream.  let the data play out, then
            // stop and close the line.
            if (thread != null) {
                line.drain();
            }
            line.stop();
            line.close();
            line = null;
            System.err.println("reached end of file");

            shutDown(null);
            restoreButtonState(); 
        }
    } // End class Playback
        

    /** 
     * Reads data from the input channel and writes to the output stream
     */
    class Capture implements Runnable {

        TargetDataLine line;
        Thread thread;
        File uploadWavFile;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            try {
                thread.setPriority(Thread.MAX_PRIORITY);
            } catch(Exception err){
            }
            thread.start();
        }
 
        public void start(File uploadWavFile) {
        	this.uploadWavFile = uploadWavFile; 
        	System.err.println("Capture uploadWavFile is:" + uploadWavFile);

            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            try {
                thread.setPriority(Thread.MAX_PRIORITY);
            } catch(Exception err){
            }
            thread.start();
        }
       
        public void stop() {
            thread = null;
        }
        
        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
                samplingGraph.stop();
                System.err.println(errStr);
                samplingGraph.repaint();
            }
        }

		public void run() {
   	
        	duration = 0;
            audioInputStream = null;
            
            // define the required attributes for our line, 
            // and make sure a compatible line is supported.

            //DEL            AudioFormat format = formatControls.getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
                format);
                        
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for capture.

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) { 
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) { 
                shutDown(ex.toString());
                //                JavaSound.showInfoDialog();
                return;
            } catch (Exception ex) { 
                shutDown(ex.toString());
                return;
            }
            ByteArrayOutputStream outbaos = new ByteArrayOutputStream();
            System.err.println("AudioFormat: " + line.getFormat());

            BufferedOutputStream out;
            try {
           	out = new BufferedOutputStream(new FileOutputStream(
					wavFile
						));
         	
            } catch (Exception e) {
                shutDown("Unable to open the output stream\n" + e);
                return;
            }

            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;
            
            line.start();

            totalBytesWritten = 0L;
            try {
				while (thread != null) {
					if((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
						break;
					}
					totalBytesWritten += numBytesRead;
					out.write(data, 0, numBytesRead);
					outbaos.write(data, 0, numBytesRead);
				}
			} catch(IOException err){
				System.err.println("IOException while writing WAV cache file: " + err);
			}

			// debug System.err.println("thread==null, recording thread about to tidy up");

            // we reached the end of the stream.  stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // load bytes into the audio input stream for playback
            getAudioInputStream();
      	
 //           saveToFile(this.uploadWavFile, AudioFileFormat.Type.WAVE);
            try {
                if (AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, uploadWavFile) == -1) {
                    throw new IOException("Problems writing to file");
                }
            } catch (Exception ex) { reportStatus(ex.toString()); }
            // debug	System.err.println("About to load bytes to byte array");
        	byte audioBytes[] = outbaos.toByteArray();
 	
        	duration = totalBytesWritten / (double) (format.getSampleRate() * format.getSampleSizeInBits()/ 8);
    		System.err.println("capture duration:" + duration);
        	// debug	System.err.println("Calculated duration");
            samplingGraph.createWaveForm(audioBytes);
            // debug System.err.println("Created samplingGraph");
            // This is the only way to "reset" long streams - re-grab
            getAudioInputStream();

            progBar.setStringPainted(true);
            progBar.setString(samplingGraph.peakWarning ? 
            		peakWarningLabel : "");
        }
    } // End class Capture
 
    /**
	 * Will free audio input stream if exists, and grab a new version. This seems a bit silly but there's no other way to "rewind" large files.
	 * @uml.property  name="audioInputStream"
	 */
    private void getAudioInputStream(){
		if (audioInputStream != null) {
			try {
				audioInputStream.close();
			} catch (IOException err) {
			}
		}
		// debug System.err.println("getAudioInputStream - totalBytesWritten:" + totalBytesWritten);
		try {
			//            	audioInputStream = AudioSystem.getAudioInputStream(wavFile);

			audioInputStream = new AudioInputStream(new BufferedInputStream(
					new FileInputStream(wavFile)), format, totalBytesWritten
					/ (format.getChannels() * format.getSampleSizeInBits() / 8) // Length in sample frames
			);

 			
		} catch (Exception err) {
			System.err.println("Exception while reading cache file: " + err);
		}
		// debug System.err.println("Grabbed audio input stream from cache file");
	}

    
    /** 
     * uploads the file
     */
    class ConvertAndUpload implements Runnable {

        Thread thread;
    		
        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("ConvertAndUpload");
    		System.err.println("=== Upload ===");
            thread.start();
        }

        public void stop() {
            thread = null;
        }
        
		public void run() {
            progBar.setVisible(true);
            progBar.setStringPainted(true);
            progBar.setMaximum(100);
            progBar.setString(uploadingMessageLabel);
            progBar.setIndeterminate(false);
            progBar.setMinimum(0);
            sentBytes = 0;
			try {
				destinationURL = new URL(destinationURL.toString());	  
			}catch(Exception err){
			    System.err.println(err);
			}
			System.err.println("Destination URL is " + destinationURL);
			

			//############ audio files ####################################
			File[] files = new File[numberofPrompts + 4];
	        for (int i = 0; i < numberofPrompts; i++) {
				files[i] = uploadWavFileA[i];
	        }
			//############ prompt files #################################### 
			try {
				BufferedWriter out_prompts = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(promptsFile),"UTF-8"));
		        for (int i = 0; i < numberofPrompts; i++) {
					out_prompts.write(promptidA[i] + " " + promptA[i] + System.getProperty("line.separator"));
		        }
			    out_prompts.close();
			} catch (IOException e) {
				    System.err.println("Problems with prompts");
			} 
			files[numberofPrompts] = promptsFile;
			//############ ReadMe file#################################### 
			try {
				BufferedWriter out_readme = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(readmeFile),"UTF-8"));
		
				out_readme.write("User Name:" + userName + System.getProperty("line.separator"));
				out_readme.write(System.getProperty("line.separator"));	
			
				out_readme.write("Speaker Characteristics:" + System.getProperty("line.separator") );
				out_readme.write(System.getProperty("line.separator"));	
				out_readme.write("Gender: " + gender + System.getProperty("line.separator") );
				out_readme.write("Age Range: " + ageRange + System.getProperty("line.separator")); 
				out_readme.write("Language: " + language + System.getProperty("line.separator"));	
				out_readme.write("Pronunciation dialect: " + dialect + System.getProperty("line.separator"));	
				out_readme.write(System.getProperty("line.separator"));
			
				out_readme.write("Recording Information:" + System.getProperty("line.separator"));	
				out_readme.write(System.getProperty("line.separator"));
				out_readme.write("Microphone make: n/a" + System.getProperty("line.separator"));	
				out_readme.write("Microphone type: " + microphone + System.getProperty("line.separator"));	
				out_readme.write("Audio card make: unknown" + System.getProperty("line.separator"));	
				out_readme.write("Audio card type: unknown" + System.getProperty("line.separator"));
				out_readme.write("Audio Recording Software: VoxForge Speech Submission Application" + System.getProperty("line.separator"));
				out_readme.write("O/S:" + System.getProperty("line.separator"));	
				out_readme.write(System.getProperty("line.separator"));	
				
				out_readme.write("File Info:" + System.getProperty("line.separator"));
				out_readme.write(System.getProperty("line.separator"));
				out_readme.write("File type: " + fileType + System.getProperty("line.separator"));
				out_readme.write("Sampling Rate: " + samplingRate + System.getProperty("line.separator"));
				out_readme.write("Sample rate format: " + samplingRateFormat + System.getProperty("line.separator"));
				out_readme.write("Number of channels: " + numberChannels + System.getProperty("line.separator"));	
				
				out_readme.close();	
			} catch (IOException e) {
			    System.err.println("Problems with Gender, Age Range or Dialect");
			} 
			files[numberofPrompts + 1] = readmeFile;
			//############ License Notice File ####################################    
			try {
				BufferedWriter out_licenseNoticeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(licenseNoticeFile),"UTF-8"));
				// !!!!!!
				//Calendar cal = Calendar.getInstance();
				//String licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + copyrightName + System.getProperty("line.separator") 
				//	+ System.getProperty("line.separator") 
				//	+ licenseObject.getBlanklicenseNotice();
				//System.err.println("!!!!!!ConvertAndUpload License notice: " + licenseNotice + "\n!!!!!!");		
				// !!!!!!
				out_licenseNoticeFile.write(licenseNotice);
				out_licenseNoticeFile.close();	
			} catch (IOException e) {
				    System.err.println("Problems with licenseNoticeFile file");
			} 
			files[numberofPrompts + 2] = licenseNoticeFile;
			//############ license file ####################################    
			try {
				BufferedWriter out_licenseFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(licenseFile),"UTF-8"));
				out_licenseFile.write(License.getGPLLicense());
				out_licenseFile.close();	
			} catch (IOException e) {
				System.err.println("Problems with license file");
			} 
			files[numberofPrompts + 3] = licenseFile;
			//############ create archive file #################################### 
			File archiveFile;
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DATE);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			StringBuffer sb = new StringBuffer(11);
			sb.append(year);
			if( month < 10 ) sb.append("0");
			sb.append(month);
			if( day < 10 ) sb.append("0");
			sb.append(day);
			String date = sb.toString();
			
			Random randomGenerator = new Random();
			int SMALL_LETTERS_BASE_VALUE = 97;
			StringBuffer randomID = new StringBuffer("");  // required for jvm 1.4.2
			// !!!!!!
			//for ( int i = 0; randomID.length() < 3; i++ )
			for ( int i = 0; i < 3; i++ )
			// !!!!!!!
			{
			   	int currentValue = randomGenerator.nextInt(26);
				currentValue += SMALL_LETTERS_BASE_VALUE;//convert to ASCII value for a-z
				randomID.append((char)currentValue);
			}
			
			if (userName != null){
				archiveFile = new File(tempdir + language + "-" + userName + "-" + date + "-" + randomID + ".zip");
			} else {
				archiveFile = new File(tempdir + language + "-" + "Anonymous-" + date + "-" + randomID + ".zip");
			}
			
			createZipArchive(archiveFile, files);
			System.err.println("Archive file location:" + archiveFile);
			totalBytes = ((int)archiveFile.length()) ; 
			progBar.setMaximum((int)archiveFile.length());
			//############ Upload #################################### 
			// Upload manager needs an array but JavaUpload.php script can only handle one file at a time
			File[] archiveFiles = new File[1];
			archiveFiles[0] = archiveFile;
            UploadManager u;
            try {
                u = new UploadManager(archiveFiles, capturePlayback, destinationURL, 1, fileFieldName);
            } catch(java.lang.NullPointerException npered){
            	u = new UploadManager(archiveFiles, capturePlayback, destinationURL, fileFieldName);
            }
            System.err.println("Uploading to " + destinationURL);
            u.start();
        }

        protected void createZipArchive(File archiveFile, File[] tobeZippedFiles) {
          try {
            byte buffer[] = new byte[BUFFER_SIZE];
            // Open archive file
            FileOutputStream stream = new FileOutputStream(archiveFile);
            ZipOutputStream out = new ZipOutputStream(stream);

            for (int i = 0; i < tobeZippedFiles.length; i++) {
              if (tobeZippedFiles[i] == null || !tobeZippedFiles[i].exists()
                  || tobeZippedFiles[i].isDirectory())
                continue;
              System.out.println("Adding " + tobeZippedFiles[i].getName());

              // Add archive entry
              ZipEntry zipAdd = new ZipEntry(tobeZippedFiles[i].getName());
              zipAdd.setTime(tobeZippedFiles[i].lastModified());
              out.putNextEntry(zipAdd);

              // Read input & write to output
              FileInputStream in = new FileInputStream(tobeZippedFiles[i]);
              while (true) {
                int nRead = in.read(buffer, 0, buffer.length);
                if (nRead <= 0)
                  break;
                out.write(buffer, 0, nRead);
              }
              in.close();
            }

            out.close();
            stream.close();
            System.out.println("Adding completed OK");
          } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return;
          }
        }
    }

    /**
     * Render a WaveForm.
     */
    class SamplingGraph extends JPanel implements Runnable {
        private Thread thread;
        //private Font font10 = new Font("serif", Font.PLAIN, 10);
        private final Font font12 = new Font("serif", Font.PLAIN, 12);
        Color jfcBlue = new Color(204, 204, 255);
        Color pink = new Color(255, 175, 175);
        protected boolean peakWarning = false;

        public SamplingGraph() {
            setBackground(new Color(20, 20, 20));
        }

        public void clearWaveForm() {
            lines.removeAllElements();  // clear the old vector
            repaint();
        }
            
        public void createWaveForm(byte[] audioBytes) {

            lines.removeAllElements();  // clear the old vector

            AudioFormat format = audioInputStream.getFormat();
            if (audioBytes == null) {
                try {
                    audioBytes = new byte[
                        (int) (audioInputStream.getFrameLength() 
                        * format.getFrameSize())];
                    audioInputStream.read(audioBytes);
                } catch (Exception ex) { 
                    reportStatus(ex.toString());
                    return; 
                }
            }

            Dimension d = getSize();
            int w = d.width;
            int h = d.height-15;
            int[] audioData = null;
            int audioDatum = 0; // Will store values one-by-one
            long numPeakValues = 0; // To help detect peaking
            int peakThresh = (int)(Math.pow(2, format.getSampleSizeInBits()) * 0.4);
 //           long nlengthInSamples = ais.getFrameLength() * ais.getFormat().getChannels();
            int nlengthInSamples =0;
            if (format.getSampleSizeInBits() == 16) {
                // int nlengthInSamples = audioBytes.length / 2;
                 nlengthInSamples = audioBytes.length / 2;
                 audioData = new int[nlengthInSamples];
                 if (format.isBigEndian()) {
                    for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is MSB (high order) */
                         int MSB = audioBytes[2*i];
                         /* Second byte is LSB (low order) */
                         int LSB = audioBytes[2*i+1];
                         audioData[i] = MSB << 8 | (255 & LSB);
						 audioDatum = MSB << 8 | (255 & LSB);
						 if(audioDatum > peakThresh) {
							 numPeakValues++;
						 }
                     }
                 } else {
                     for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is LSB (low order) */
                         int LSB = audioBytes[2*i];
                         /* Second byte is MSB (high order) */
                         int MSB = audioBytes[2*i+1];
                         audioData[i] = MSB << 8 | (255 & LSB);
						 audioDatum = MSB << 8 | (255 & LSB);
						 if(audioDatum > peakThresh) {
							 numPeakValues++;
						 }
                     }
                 }
             } 
               
            int frames_per_pixel = audioBytes.length / format.getFrameSize()/w;
            byte my_byte = 0;
            double y_last = 0;
            int numChannels = format.getChannels();
            for (double x = 0; x < w && audioData != null; x++) {
                int idx = (int) (frames_per_pixel * numChannels * x);
                if (format.getSampleSizeInBits() == 8) {
                     my_byte = (byte) audioData[idx];
                } else {
                     my_byte = (byte) (128 * audioData[idx] / 32768 );
                }
                double y_new = h * (128 - my_byte) / 256;
                lines.add(new Line2D.Double(x, y_last, x, y_new));
                y_last = y_new;
            }
            System.err.println("numPeakValues: " + numPeakValues);
            float proportionPeakValues = ((float)numPeakValues) / (nlengthInSamples);
            System.err.println("proportionPeakValues: " + proportionPeakValues);
            peakWarning = proportionPeakValues > 0.001f;
            repaint();
        }

        @Override
		public void paint(Graphics g) {

            Dimension d = getSize();
            int w = d.width;
            int h = d.height;
            int INFOPAD = 15;

            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
            g2.setColor(Color.white);
            g2.fillRect(0, h-INFOPAD, w, INFOPAD);

            if (errStr != null) {
                g2.setColor(jfcBlue);
                g2.setFont(new Font("serif", Font.BOLD, 18));
                g2.drawString("ERROR", 5, 20);
                AttributedString as = new AttributedString(errStr);
                as.addAttribute(TextAttribute.FONT, font12, 0, errStr.length());
                AttributedCharacterIterator aci = as.getIterator();
                FontRenderContext frc = g2.getFontRenderContext();
                LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
                float x = 5, y = 25;
                lbm.setPosition(0);
                while (lbm.getPosition() < errStr.length()) {
                    TextLayout tl = lbm.nextLayout(w-x-5);
                    if (!tl.isLeftToRight()) {
                        x = w - tl.getAdvance();
                    }
                    tl.draw(g2, x, y += tl.getAscent());
                    y += tl.getDescent() + tl.getLeading();
                }
            } else if (capture.thread != null) {
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString(sampleGraphLengthLabel + String.valueOf(seconds), 3, h-4);
            } else {
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString(sampleGraphFileLabel + fileName + sampleGraphLengthLabel + String.valueOf(duration) + sampleGraphPositionLabel + String.valueOf(seconds), 3, h-4);

                if (audioInputStream != null) {
                    // .. render sampling graph ..
                    g2.setColor(jfcBlue);
                    for (int i = 1; i < lines.size(); i++) {
                        g2.draw((Line2D) lines.get(i));
                    }

                    // .. draw current position ..
// debug            System.err.println("seconds "+seconds+";duration:"+duration+":");
                    if (seconds != 0) {
                        double loc = seconds/duration*w;
                        g2.setColor(pink);
                        g2.setStroke(new BasicStroke(3));
                        g2.draw(new Line2D.Double(loc, 0, loc, h-INFOPAD-2));
                    }
                }
            }
        }
    
        public void start() {
            thread = new Thread(this);
            thread.setName("SamplingGraph");
            thread.start();
            seconds = 0;
        }

        public void stop() {
            if (thread != null) {
                thread.interrupt();
            }
            thread = null;
        }

		public void run() {
            seconds = 0;
            while (thread != null) {
                if ((playback.line != null) && (playback.line.isOpen()) ) {

                    long milliseconds = playback.line.getMicrosecondPosition() / 1000;

                    seconds =  milliseconds / 1000.0;
    // debug        System.err.println("playback.line:seconds "+seconds+";milliseconds:"+milliseconds+":");
                } else if ( (capture.line != null) && (capture.line.isActive()) ) {

                    long milliseconds = capture.line.getMicrosecondPosition() / 1000;
                    seconds =  milliseconds / 1000.0;
    // debug        System.err.println("capture.line:seconds "+seconds+";milliseconds:"+milliseconds+":");

                }

                //try { thread.sleep(100); } catch (Exception e) { break; }
                try { Thread.sleep(100); } catch (Exception e) { break; }
                
                repaint();
                                
                while ((capture.line != null && !capture.line.isActive()) ||
                       (playback.line != null && !playback.line.isOpen())) 
                {
                    //try { thread.sleep(10); } catch (Exception e) { break; }
                    try { Thread.sleep(10); } catch (Exception e) { break; }
                }
            }
            seconds = 0;
            repaint();
        }
    } // End class SamplingGraph



    // Methods called by the "postlet" UploadManager and UploadThread
	public synchronized void setProgress(int a) {
        sentBytes += a;
        progBar.setValue(sentBytes);
        if (sentBytes == totalBytes){
            //DEL progCompletion.setText(pLabels.getLabel(2));
            if (endPageURL != null){
            	progBar.setString(uploadCompletedMessageLabel);
                progBar.setIndeterminate(false);
                System.err.println("Finished! Sending you on to "+endPageURL);
                ((JApplet)(getParent().getParent().getParent().getParent())).getAppletContext().showDocument(endPageURL);
            } else {
                // Just ignore this error, as it is most likely from the endpage
                // not being set.
                // Attempt at calling Javascript after upload is complete.
            	//FORDEBUG
                System.err.println("setProgress(): endPageURL is null, so trying to use the javascript hook to end.");
                JSObject win = JSObject.getWindow((JApplet)getParent());
                win.eval("postletFinished();");
            }
            // Reset the applet
            progBar.setValue(0);
          //  if(subject != null) { // Will be non-null if passed in as a param - even if blank
          //    subjectBox.setEnabled(true);
          //  }
        } else {
        	// !!!!!!
        	//FORDEBUG
        	// debug   System.err.println("setProgress(): Not reached end yet. sentBytes="+sentBytes+", totalBytes="+totalBytes);
        	// !!!!!!
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
    
	public String getCookie(){
		// If passed in as a param then we don't need to worry about trying to fetch it from the context
	//	System.err.println("CapturePlayback Cookie: " + cookie +":\n");  
	//	System.err.println("CapturePlayback Cookie: " + this.cookie +":\n");
    	if (cookie != null && !cookie.equals(""))
			return cookie;
		// Method reads the cookie in from the Browser using the LiveConnect object.
		// May also add an option to set the cookie using an applet parameter 
//		JSObject win = (JSObject) JSObject.getWindow((JApplet) this.getParent());
//		cookie = "" + (String) win.eval("document.cookie");
		return cookie;
	}
  

} 