/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template data, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karda
 */
public class FileUtils {
    List<String> lines = new ArrayList();
    String datafolder = System.getenv("APPDATA");
    File data = new File(datafolder + File.separator + "KardasLand AntiUSB" + File.separator + "cache.kd");
    
    public boolean checkFile() throws IOException {
        if (!data.exists()) {
            File folder = new File(datafolder + File.separator + "KardasLand AntiUSB");
            folder.mkdirs();
            data.createNewFile();
            return false;
        } else {
            return true;
        }
    }

    public boolean isFileNotEmpty() {
        try {
            this.lines = Files.readAllLines(data.toPath(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getHash() {
        return lines.get(0);
    }
    public int getActive() {
        return Integer.parseInt(lines.get(1));
    }
    
    public boolean writeFile(String hash){
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(data, false);
            myWriter.write(hash + "\n0");
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                myWriter.close();
                isFileNotEmpty();
                return true;
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
    public boolean writeFile(String hash, int state) {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(data, false);
            myWriter.write(hash + "\n"+state);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                myWriter.close();
                return true;
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
    
    public void changeActiveState(int state){
        String hash = getHash();
        writeFile(hash, state);
    }
}
