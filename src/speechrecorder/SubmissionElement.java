package speechrecorder;

import java.io.File;

public class SubmissionElement {
	public String promptid;
	public String prompt;
    public File wavFile; // raw audio
    public File uploadWavFile; // wav file with header
    
    /**
     * constructor 
     * 
     * @param tempdir
     * @param promptid
     * @param prompt
     */
	public SubmissionElement(
			String tempdir,
			String promptid,
			String prompt
    ) 
	{
		this.promptid = promptid;
		this.prompt = prompt;
		
		createRawAndWavFiles(tempdir);
	}
	
    /**
     * Create Raw and WAV files to hold recordings
     * Audio recorder (Capture class) records to a raw file, then we copy
     * to a WAV file (which adds headers to the wav file)
     * 
     * @param numberofPrompts
     * @param promptidA
     */
    private void createRawAndWavFiles(String tempdir) 
    { 
    	wavFile = new File(tempdir + "wavFile" + promptid + ".wav");
    	wavFile.deleteOnExit();
        
		uploadWavFile = new File(tempdir + promptid + ".wav");
		uploadWavFile.deleteOnExit();
    }
    
	/**
	 * returns promptId and prompt sentence in string format
	 */
	public String toString()
	{
		return promptid + " " + prompt + System.getProperty("line.separator");
	}
}
