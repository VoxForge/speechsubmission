package speechrecorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JProgressBar;

import net.sf.postlet.UploadManager;


/** 
 * uploads the file
 */
class SaveOrUpload implements Runnable {
    Thread thread;
    String fileFieldName = "userfile";         
    JProgressBar progBar;
    
    CapturePlayback capturePlayback;
    
    URL destinationURL;
    String uploadingMessageLabel;
    int sentBytes;
    int numberofPrompts;
    File [] uploadWavFileA;
    String [] promptidA;
    String [] promptA;
    String userData;
	String tempdir;
	String licenseNotice;
    int buffer_size;
    String language;
    String userName;
    
    File promptsFile;  
    File readmeFile;    
    File licenseFile;   
    File licenseNoticeFile;   
    
    int totalBytes; // return value
    
    // constructor
    public SaveOrUpload(
			CapturePlayback capturePlayback,
			URL destinationURL, 
			String uploadingMessageLabel, 
			int numberofPrompts,
			File [] uploadWavFileA,
		    String [] promptidA,
		    String [] promptA,
		    String tempdir,
		    String licenseNotice,
		    int buffer_size,
		    String language
    	) 
	{   
    	this.capturePlayback = capturePlayback;   
    	this.destinationURL = destinationURL;
    	this.uploadingMessageLabel = uploadingMessageLabel;
    	this.numberofPrompts = numberofPrompts;    	
    	this.uploadWavFileA = uploadWavFileA;    	
    	this.promptidA = promptidA;    	
    	this.promptA = promptA;    	
    	this.tempdir = tempdir;    	
    	this.licenseNotice = licenseNotice;       	
    	this.buffer_size = buffer_size;      

    	this.language = language;   
    	
		promptsFile = new File(tempdir + "prompts.txt");			
		promptsFile.deleteOnExit();		
		readmeFile = new File(tempdir + "readme.txt");			
		readmeFile.deleteOnExit();		
		licenseFile = new File(tempdir + "GPL_license.txt");			
		licenseFile.deleteOnExit();			
		licenseNoticeFile = new File(tempdir + "license.txt");	
		licenseNoticeFile.deleteOnExit();	
	}
    
    public int start(JProgressBar progBar, String userName, String userData) {
        thread = new Thread(this);
        thread.setName("ConvertAndUpload");
		System.err.println("=== Upload ===");
		System.err.println("destinationURL:" + destinationURL);
        thread.start();
        
    	this.userName = userName;   
    	this.userData = userData;   
        this.progBar = progBar;
        
        return totalBytes;
    }

    public void stop() {
        thread = null;
    }
    
	public void run() {
        progBar.setVisible(true);
        progBar.setStringPainted(true);
        progBar.setMaximum(100);
		System.err.println("uploadingMessageLabel:" + uploadingMessageLabel);
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
			out_readme.write( userData );
			out_readme.close();	
		} catch (IOException e) {
		    System.err.println("Problems with Gender, Age Range or Dialect");
		} 
		files[numberofPrompts + 1] = readmeFile;
		//############ License Notice File ####################################    
		try {
			BufferedWriter out_licenseNoticeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(licenseNoticeFile),"UTF-8"));
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
		for ( int i = 0; i < 3; i++ )
		{
		   	int currentValue = randomGenerator.nextInt(26);
			currentValue += SMALL_LETTERS_BASE_VALUE;//convert to ASCII value for a-z
			randomID.append((char)currentValue);
		}
		
		String zipfilename = tempdir + language + "-" + userName + "-" + date + "-" + randomID + ".zip";
		if (userName != null){
			archiveFile = new File(zipfilename);
		} else {
			archiveFile = new File(zipfilename);
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
        try 
        {
            u = new UploadManager(archiveFiles, capturePlayback, destinationURL, 1, fileFieldName);
        } 
        catch(java.lang.NullPointerException npered)
        {
        	u = new UploadManager(archiveFiles, capturePlayback, destinationURL, fileFieldName);
        }
        System.err.println("Uploading to " + destinationURL);
        u.start();
    }
	
    void createZipArchive(File archiveFile, File[] tobeZippedFiles) {
        try {
          byte buffer[] = new byte[buffer_size];
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
	
    /** 
     * saves the submission locally
     */
	/*
    class ConvertAndSavelocally  implements Runnable {

        Thread thread;
        String targetDirectory;
        
        public void start(String targetDirectory) {
            errStr = null;
            thread = new Thread(this);
            thread.setName("ConvertAndSavelocally");
    		System.err.println("=== Save Local ===");
    		this.targetDirectory = targetDirectory;
            thread.start();
        }

        public void stop() {
            thread = null;
        }
        
		public void run() {
            progBar.setVisible(true);
            progBar.setStringPainted(true);
            progBar.setMaximum(100);
            progBar.setString("Saving locally");
            progBar.setIndeterminate(false);
            progBar.setMinimum(0);
            sentBytes = 0;

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
				out_readme.write( userDataToString() );
				out_readme.close();	
			} catch (IOException e) {
			    System.err.println("Problems with Gender, Age Range or Dialect");
			} 
			files[numberofPrompts + 1] = readmeFile;
			//############ License Notice File ####################################    
			try {
				BufferedWriter out_licenseNoticeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(licenseNoticeFile),"UTF-8"));
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
			for ( int i = 0; i < 3; i++ )
			{
			   	int currentValue = randomGenerator.nextInt(26);
				currentValue += SMALL_LETTERS_BASE_VALUE;//convert to ASCII value for a-z
				randomID.append((char)currentValue);
			}
			
			String zipfilename = tempdir + language + "-" + userName + "-" + date + "-" + randomID + ".zip";
			if (userName != null){
				archiveFile = new File(zipfilename);
			} else {
				archiveFile = new File(zipfilename);
			}
			
			createZipArchive(archiveFile, files);
			System.err.println("Archive file location:" + archiveFile);
			totalBytes = ((int)archiveFile.length()) ; 
			progBar.setMaximum((int)archiveFile.length());

			File targetFile = new File(this.targetDirectory + language + "-" + userName + "-" + date + "-" + randomID + ".zip");
	        try {
	        	copyFile(archiveFile, targetFile);
				System.err.println("target file location:" + targetFile);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Error: cant copy zip file to target folder" + e.getMessage());
	        }

	        setProgress((int)targetFile.length());
        }
    }
    
	 // from: http://stackoverflow.com/questions/106770/standard-concise-way-to-copy-a-file-in-java 
	 // see: http://docs.oracle.com/javase/6/docs/api/java/nio/channels/FileChannel.html#transferTo(long,%20long,%20java.nio.channels.WritableByteChannel)
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}  
    
*/    

    
    
    
}
