package speechrecorder;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.swing.JProgressBar;

import net.sf.postlet.PostletInterface;

public class Submission {
	String userName = "unknown";
    String gender;
    String ageRange;
    String language;
    String dialect;  
    String microphone;     

    int languageIndex;
    int genderIndex;
    int ageRangeIndex;
    int dialectIndex;  
    int microphoneIndex; 
    
    private String tempdir;
	
	private int numberofPrompts;
	private ResourceBundle messages;
	
    private Prompts prompts;
    
    private SubmissionElement[] elementA;
    
    int index = 0;
    
	SaveOrUpload saveOrUpload; 
    String licenseNotice;
    String vflicense;
    
    PostletInterface postletInterface;
    
	/**
	 * constructor
	 * 
	 * @param postletInterface
	 * @param language
	 * @param numberofPrompts
	 * @param messages
	 */
	public Submission(
			PostletInterface postletInterface,
    		String language, 
    		int numberofPrompts,
    		ResourceBundle messages
		) 
	{
		this.language = language;
		this.numberofPrompts = numberofPrompts;
		this.messages = messages;
		
	    gender = messages.getString("notApplicable"); // default selection
	    ageRange = messages.getString("notApplicable"); // default selection
	    dialect = messages.getString("notApplicable");  // default selection
	    microphone = messages.getString("notApplicable");  // default selection

        tempdir = createTempDir(); 
	    
		this.prompts = new Prompts(language, numberofPrompts);
		String[][] promptArray = prompts.getPrompts();
		
		elementA = new SubmissionElement[numberofPrompts];
	    for (int i = 0; i < numberofPrompts; i++) 
	    {
	    	elementA[i] = new SubmissionElement(tempdir, promptArray[0][i], promptArray[1][i]);
	    	
			System.out.println("Submission WAV file for recording uploadWavFile" + i + " is: " + elementA[i].uploadWavFile);
			//System.out.println("Submission raw file for recording wavFileA" + i + " is: " + elementA[i].wavFile);
	    }
	}
	
	/**
	 * upload submission to VoxForge repository
	 * 
	 * @param postletInterface
	 * @param progBar
	 * @param recInfo
	 * @param destinationURL
	 * @return
	 */
	public int upload (
			PostletInterface postletInterface,
    		JProgressBar progBar, 
		    String recInfo,
    		URL destinationURL
		 )
	{
	    int totalBytes;
		
	   	Calendar cal = Calendar.getInstance();
		licenseNotice = "Copyright " + cal.get(Calendar.YEAR) + " " + messages.getString("copyrightName") + System.getProperty("line.separator") 
				+ System.getProperty("line.separator") 
				+ License.getBlanklicenseNotice();				
		vflicense = License.getVFLicense();	 	

		saveOrUpload = new SaveOrUpload(
				postletInterface,
				destinationURL, 
				messages.getString("uploadingMessageLabel"),
				this,
			    licenseNotice
		);
		//convertAndSavelocally = new ConvertAndSavelocally();
		
		totalBytes = saveOrUpload.createArchive(
				progBar, 
				language.toUpperCase(), 
				userName, 
				userDataToString(recInfo)
		);    
		saveOrUpload.upload();
		
		return totalBytes;
	}
	
	/**
	 * returns number of prompts in Submission object
	 * 
	 * @return
	 */
	public int getNumberOfPrompts()
	{
		return prompts.getNumberOfPrompts();
	}

	/**
	 * iterator: returns boolean if more prompts exist
	 * 
	 * @return
	 */
	public Boolean hasNext()
	{
		if (index < numberofPrompts)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
    
	/**
	 * Submission Element iterator
	 * 
	 * @return
	 */
	public SubmissionElement next()
	{
		SubmissionElement result = null;
		
		if (hasNext())
		{	
			result = elementA[index];
			index++;
		}
		else
		{
			System.err.println("Error: no more elements");
		}

		return result;
	}
	
	/**
	 * get Submission Element with given index number
	 * 
	 * @param i
	 * @return
	 */
	public SubmissionElement getElement(int i)
	{
		if (i < numberofPrompts)
		{
			return elementA[i];
		}
		else
		{
			System.err.println("Error: invalid element index");
			return null;
		}
	}
   
    /**
     * create temporary directory and return path as a string; 
     * 
     * creates new temp dir with every call
     * 
     * @return
     */
    private String createTempDir() {
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

	/**
	 * return name of directory where wav files are stored
	 * 
	 * @return
	 */
	public String getDirectory()
	{
		return tempdir;
	}
    
    /**
     * return list of promptIDs and prompt sentences in string format
     */
	public String toString()
	{
		String result = "";
		
        for (int i = 0; i < numberofPrompts; i++) {
			result = result + elementA[i].toString();
        }
        
        return result;
	}

    /**
     * convert user data to a String
     * 
     * @return
     */
    public String userDataToString (String recInfo) {
		String userData = "";
		
		userData = "User Name:" + userName + System.getProperty("line.separator");
		userData = userData + System.getProperty("line.separator");	

		userData = userData + "Speaker Characteristics:" + System.getProperty("line.separator");
		userData = userData + System.getProperty("line.separator");	
		userData = userData + "Gender: " + gender + System.getProperty("line.separator");
		userData = userData + "Age Range: " + ageRange + System.getProperty("line.separator"); 
		userData = userData + "Language: " + language.toUpperCase() + System.getProperty("line.separator");	
		userData = userData + "Pronunciation dialect: " + dialect + System.getProperty("line.separator");	
		userData = userData + System.getProperty("line.separator");
	
		userData = userData + "Recording Information:" + System.getProperty("line.separator");	
		userData = userData + System.getProperty("line.separator");
		userData = userData + "Microphone make: n/a" + System.getProperty("line.separator");	
		userData = userData + "Microphone type: " + microphone + System.getProperty("line.separator");	
		userData = userData + "Audio card make: unknown" + System.getProperty("line.separator");	
		userData = userData + "Audio card type: unknown" + System.getProperty("line.separator");
		userData = userData + "Audio Recording Software: standalone VoxForge speech submission application" + System.getProperty("line.separator");
		userData = userData + "O/S:" + System.getProperty("line.separator");	
		userData = userData + System.getProperty("line.separator");	

		userData = userData + recInfo;
				
		return userData;
	}
	
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
	    // see   java.util.regex.Pattern: \W  A non-word character: [^\w]
		this.userName = (userName.replaceAll("\\W",""));
		
		if (userName.length() == 0 ) 
		{
		    this.userName = "anonymous";
		} else 
		{
			if (userName.length() > 40 ) 
			{
				this.userName = userName.substring(0,40);
			} 
		}
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getAgeRange() {
		return ageRange;
	}


	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}


	public String getDialect() {
		return dialect;
	}


	public void setDialect(String dialect) {
		this.dialect = dialect;
	}


	public String getMicrophone() {
		return microphone;
	}


	public void setMicrophone(String microphone) {
		this.microphone = microphone;
	}
	
    public int getGenderIndex() {
		return genderIndex;
	}

	public void setGenderIndex(int genderIndex) {
		this.genderIndex = genderIndex;
	}

	public int getAgeRangeIndex() {
		return ageRangeIndex;
	}

	public void setAgeRangeIndex(int ageRangeIndex) {
		this.ageRangeIndex = ageRangeIndex;
	}

	public int getLanguageIndex() {
		return languageIndex;
	}

	public void setLanguageIndex(int languageIndex) {
		this.languageIndex = languageIndex;
	}

	public int getDialectIndex() {
		return dialectIndex;
	}

	public void setDialectIndex(int dialectIndex) {
		this.dialectIndex = dialectIndex;
	}

	public int getMicrophoneIndex() {
		return microphoneIndex;
	}

	public void setMicrophoneIndex(int microphoneIndex) {
		this.microphoneIndex = microphoneIndex;
	}

    
/*
    protected void saveLocal() 
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
}