package speechrecorder;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JProgressBar;

/**
 * Write data to the OutputChannel.
 */
public class Playback implements Runnable {
    SourceDataLine line;
    Thread thread;

    CapturePlayback capturePlayback;
	AudioFormat format;
    int numberofPrompts;
	String peakWarningLabel;
	String sampleGraphFileLabel;
    String sampleGraphLengthLabel; 
    String sampleGraphPositionLabel;
    String playButton;
    String stopButton;
    int bufSize;       
    
    AudioInputStream audioInputStream;
    SamplingGraph samplingGraph;
	JProgressBar progBar;
    JButton [] playA; 
    JButton [] captA;
    String fileName;
    double duration;
    
    String errStr;

    public Playback (
    		CapturePlayback capturePlayback,  
    		AudioFormat format,
    		int numberofPrompts,
            int bufSize,
    		String peakWarningLabel,
    		String sampleGraphFileLabel,
            String sampleGraphLengthLabel, 
            String sampleGraphPositionLabel,
            String playButton,
            String stopButton
    	)
    {
    	this.capturePlayback = capturePlayback; 
    	this.format = format; 
    	this.numberofPrompts = numberofPrompts;     
    	this.peakWarningLabel = peakWarningLabel; 
    	this.sampleGraphFileLabel = sampleGraphFileLabel;   
    	this.sampleGraphLengthLabel = sampleGraphLengthLabel; 
    	this.sampleGraphPositionLabel = sampleGraphPositionLabel; 
    	this.playButton = playButton;
    	this.stopButton = stopButton;
    	this.bufSize = bufSize;
    }
    
    public void start(
    		SamplingGraph samplingGraph,
    		JProgressBar progBar,
            JButton [] playA, 
            JButton [] captA,
            String fileName,
            double duration
    	) 
    {
    	this.samplingGraph = samplingGraph; 
    	this.progBar = progBar;
    	this.playA = playA; 
    	this.captA = captA;
    	this.fileName = fileName; 
    	this.duration = duration;
    	
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
		audioInputStream = capturePlayback.getAudioInputStream();

        // get an AudioInputStream of the desired format for playback
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
        samplingGraph.createWaveForm(
        		audioInputStream, 
        		audioBytes, 
    		    fileName,
        		duration
        ); 
        samplingGraph.repaint();
        
        audioInputStream = capturePlayback.getAudioInputStream();
        playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
        
        progBar.setStringPainted(true);
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
        System.out.println("reached end of audio file");

        shutDown(null);
        capturePlayback.restoreButtonState(); 
    }
} 
