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
 */
public class PlaySoundTask implements ISoundThread {
    
    private volatile boolean isRunning = false;
    
    private final File wavFile;
    private Clip audioClip;
    
    public PlaySoundTask(File wavFile) {
        this.wavFile = wavFile;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void kill() {
        this.audioClip.close();
    }

    @Override
    public File getWavFile() {
        return this.wavFile;
    }

    @Override
    public Object call() throws Exception {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(this.wavFile);
 
            AudioFormat format = ais.getFormat();
 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
 
            this.audioClip = (Clip) AudioSystem.getLine(info);
  
            this.audioClip.open(ais);
            
            this.audioClip.start();

            System.out.println("PLAYING");
            
            return this.audioClip;
               
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
        return null;
    }
    
}
