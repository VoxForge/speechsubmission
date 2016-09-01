package speechrecorder;

import java.io.File;
import java.util.List;

public class Submission {
	String language;
	int numberofPrompts;
	
    Prompts prompts;
	
    
    SubmissionElement[] elementA;
    //String [] promptA;
    //String [] promptidA;
    
    private File[] wavFileA; // raw audio
    private File [] uploadWavFileA; // wav file with header
    
    int index = 0;
    
	public Submission(
    		String language, 
    		int numberofPrompts
    ) {
		this.language = language;
		this.numberofPrompts = numberofPrompts;
		
		this.prompts = new Prompts(language, numberofPrompts);
		String[][] promptArray = prompts.getPrompts();
		
		elementA = new SubmissionElement[numberofPrompts];

	    for (int i = 0; i < numberofPrompts; i++) 
	    {
	    	elementA[i] = new SubmissionElement(promptArray[0][i], promptArray[1][i]);
	    }
	    
	}
	
	public int getNumberOfPrompts()
	{
		return prompts.getNumberOfPrompts();
	}

	
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
    
	public SubmissionElement next()
	{
		SubmissionElement result = null;
		
		if (this.hasNext())
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
}
