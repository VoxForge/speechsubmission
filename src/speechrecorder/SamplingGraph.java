package speechrecorder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JPanel;

import speechrecorder.Capture;
import speechrecorder.Playback;

/**
 * Render a WaveForm.
 */
@SuppressWarnings("serial")
class SamplingGraph extends JPanel implements Runnable {
    private Thread thread;
    private final Font font12 = new Font("serif", Font.PLAIN, 12);
    Color jfcBlue = new Color(204, 204, 255);
    Color pink = new Color(255, 175, 175);
    protected boolean peakWarning = false;
    
    // constructor vars
	Vector<Double> lines = new Vector<Double>();
	AudioInputStream audioInputStream;    
	Capture capture;
    Playback playback;

    // paint vars
    String sampleGraphFileLabel;
	String sampleGraphLengthLabel;
	String sampleGraphPositionLabel;
    String fileName;
    double duration;
    
    double seconds;	
	String errStr;

    public SamplingGraph (
    		Vector<Double> lines,
    		Capture capture,
    		Playback playback
    		) 
    {
    	setBackground(new Color(20, 20, 20));
        this.lines = lines;
        this.capture = capture;
        this.playback = playback;
    }   
    
    public void createWaveForm(
    			AudioInputStream audioInputStream, 
    			byte[] audioBytes, 
    		    String sampleGraphFileLabel,
    			String sampleGraphLengthLabel,
    			String sampleGraphPositionLabel,
    		    String fileName,
    			double duration
    		) 
    {
        this.audioInputStream = audioInputStream;    	
        this.sampleGraphFileLabel = sampleGraphFileLabel;
        this.sampleGraphLengthLabel = sampleGraphLengthLabel;       
        this.sampleGraphPositionLabel = sampleGraphPositionLabel;
        this.fileName = fileName;  
        this.duration = duration;  
        
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
        
        repaint(); // calls paint
    }

    @Override
	public void paint(Graphics g) 
    {
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
            } else if ( (capture.line != null) && (capture.line.isActive()) ) {

                long milliseconds = capture.line.getMicrosecondPosition() / 1000;
                seconds =  milliseconds / 1000.0;

            }

            try { Thread.sleep(100); } catch (Exception e) { break; }
            
            repaint();
                            
            while ((capture.line != null && !capture.line.isActive()) ||
                   (playback.line != null && !playback.line.isOpen())) 
            {
                try { Thread.sleep(10); } catch (Exception e) { break; }
            }
        }
        seconds = 0;
        repaint();
    }
	
    private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
            this.repaint();
        }
    }
} 

