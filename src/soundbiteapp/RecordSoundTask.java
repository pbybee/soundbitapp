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
    
    private volatile boolean isRunning = true;
    
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private TargetDataLine line;
    private final File wavFile;

    public RecordSoundTask(String wavFileName) {
        String cwd = System.getProperty("user.dir");
        wavFile = new File(cwd + "\\" + wavFileName);
    }
    
    
    @Override
    public void run() {
        try {
            if (isRunning) {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // checks if system supports the data line
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();   // start capturing

                AudioInputStream ais = new AudioInputStream(line);

                // start recording
                AudioSystem.write(ais, fileType, wavFile);
            }
            
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void kill() {
        isRunning = false;
        line.stop();
        line.close();
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
}
