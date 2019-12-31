/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundbiteapp;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 *
 * @author ptb
 * Audio recording code adapted from 
 * https://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
 */
public class RecordSoundTask implements ISoundThread {
    
    private volatile boolean isRunning = false;
        
    private final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private TargetDataLine line;
    private final File wavFile;

    public RecordSoundTask(String wavFileName) {
        String cwd = System.getProperty("user.dir");
        this.wavFile = new File(cwd + File.separator + wavFileName + ".wav");
    }
    
    @Override
    public Object call() {
        try {
            this.isRunning = true;
            
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            this.line = (TargetDataLine) AudioSystem.getLine(info);
            
            this.line.open(format);
            this.line.start();   // start capturing

            AudioInputStream ais = new AudioInputStream(this.line);
            
            System.out.println("RECORDING");
            
            // start recording
            AudioSystem.write(ais, this.fileType, this.wavFile);
            
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
            this.line.stop();
            this.line.close();
        } finally {
            //I Don't think these clean ups are needed but just in case
            Thread.currentThread().interrupt();
            this.isRunning = false;
            return null;
        }
    }
    
    @Override
    public void kill() {
        this.line.stop();
        this.line.close();
    }
    
    @Override
    public File getWavFile() {
        return this.wavFile;
    }
    
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

}
