package speechrecorder;

import java.io.File;
import java.util.ResourceBundle;

public class Submission {
	String tempdir;
	
	String language;
	private int numberofPrompts;
	ResourceBundle messages;
	
    Prompts prompts;
    
    SubmissionElement[] elementA;
    
    int index = 0;
    
	public Submission(
    		String language, 
    		int numberofPrompts,
    		ResourceBundle messages
			) 
	{
		this.language = language;
		this.numberofPrompts = numberofPrompts;
		this.messages = messages;
        tempdir = createTempDir(); 
		
		this.prompts = new Prompts(language, numberofPrompts);
		String[][] promptArray = prompts.getPrompts();
		
		elementA = new SubmissionElement[numberofPrompts];
	    for (int i = 0; i < numberofPrompts; i++) 
	    {
	    	elementA[i] = new SubmissionElement(tempdir, promptArray[0][i], promptArray[1][i]);
	    	
			System.out.println("Submission WAV file for recording uploadWavFile" + i + " is: " + elementA[i].uploadWavFile);
			System.out.println("Submission raw file for recording wavFileA" + i + " is: " + elementA[i].wavFile);
	    }
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
	
}