package speechrecorder;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JProgressBar;

import speechrecorder.CapturePlayback.CaptureResult;

/** 
 * Reads data from the input channel and writes to the output stream
 */
class Capture implements Runnable {
    TargetDataLine line;
    Thread thread;

    CapturePlayback capturePlayback;
    SamplingGraph samplingGraph;
    AudioInputStream audioInputStream;
	AudioFormat format;
	JProgressBar progBar;
    File uploadWavFile;  
    File wavFile;
    String fileName;
    
    String errStr;
    long totalBytesWritten;
    double duration;
    
    String peakWarningLabel; 
    String sampleGraphFileLabel; 
    String sampleGraphLengthLabel; 
    String sampleGraphPositionLabel; 
    
    public Capture (
    		CapturePlayback capturePlayback,
    		AudioFormat format,
    		String peakWarningLabel,
    		String sampleGraphFileLabel,
            String sampleGraphLengthLabel, 
            String sampleGraphPositionLabel
    	) 
    {
    	this.capturePlayback = capturePlayback; 
    	this.format = format; 
    	this.peakWarningLabel=peakWarningLabel;
    	this.sampleGraphFileLabel=sampleGraphFileLabel;
    	this.sampleGraphLengthLabel=sampleGraphLengthLabel;
    	this.sampleGraphPositionLabel=sampleGraphPositionLabel;
    }
    
    public void start(
    		SamplingGraph samplingGraph,
    		JProgressBar progBar,
    		File uploadWavFile,
    		File wavFile,
    		String fileName
    	) 
    {
    	this.samplingGraph = samplingGraph; 
    	this.progBar = progBar; 
    	this.uploadWavFile = uploadWavFile; 
    	this.wavFile = wavFile; 
    	this.fileName = fileName; 
    	
    	System.out.println("Capture uploadWavFile is:" + uploadWavFile);

        errStr = null;
        thread = new Thread(this);
        thread.setName("Capture");
        try {
            thread.setPriority(Thread.MAX_PRIORITY);
        } catch(Exception err){
        }
        thread.start();
    }
   
    public CaptureResult stop() {
        thread = null;
        
        CaptureResult result = ( capturePlayback.new CaptureResult() );
//        result.audioInputStream = audioInputStream;
        result.duration = duration;
        result.totalBytesWritten = totalBytesWritten;
        
        return result;
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
        System.out.println("AudioFormat: " + line.getFormat());

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
        audioInputStream = capturePlayback.getAudioInputStream();
  	
        try {
            if (AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, uploadWavFile) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) { reportStatus(ex.toString()); }
        // debug	System.err.println("About to load bytes to byte array");
    	byte audioBytes[] = outbaos.toByteArray();
	
    	duration = totalBytesWritten / (double) (format.getSampleRate() * format.getSampleSizeInBits()/ 8);
		System.out.println("capture duration:" + duration);
    	// debug	System.err.println("Calculated duration");
        samplingGraph.createWaveForm(
        		audioInputStream, 
        		audioBytes, 
    		    fileName,
        		duration
        );
        // debug System.err.println("Created samplingGraph");
        // This is the only way to "reset" long streams - re-grab
        audioInputStream = capturePlayback.getAudioInputStream();

        progBar.setStringPainted(true);
    	//progBar.setString(samplingGraph.peakWarning ? 
        //		peakWarningLabel : "");
        if (samplingGraph.peakWarning)
        {
        	progBar.setBackground(Color.red);
        	progBar.setString(peakWarningLabel);
        }
        else
        {
        	progBar.setBackground(capturePlayback.getBackground());         	
        	progBar.setString("");
        }
    }

	private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
            samplingGraph.repaint();
        }
    }
} 

