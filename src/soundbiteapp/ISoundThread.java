/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundbiteapp;

import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author ptb
 * 
 * Uses callable for the ExecutorService
 */
public interface ISoundThread extends Callable {
    
    public boolean isRunning();
        
    public void kill();
    
    public File getWavFile();
    
}
