/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template data, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.utils.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karda
 */
public class FileUtils {
    List<String> lines = new ArrayList<>();
    String datafolder = System.getenv("APPDATA");
    File data = new File(datafolder + File.separator + "KardasLand AntiUSB" + File.separator + "cache.kd");
    public File config = new File(datafolder + File.separator + "KardasLand AntiUSB" + File.separator + "config.yml");
    public File language = new File(datafolder + File.separator + "KardasLand AntiUSB" + File.separator + "language.yml");
    File folder = new File(datafolder + File.separator + "KardasLand AntiUSB");

    public boolean checkDataFile() throws IOException {
        if (!data.exists()) {
            folder.mkdirs();
            data.createNewFile();
            return false;
        } else {
            return true;
        }
    }

    public InputStream checkConfigFile() throws IOException{
        if (!config.exists()){
            folder.mkdirs();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.yml");
            assert inputStream != null;
            Files.copy(inputStream, Paths.get(config.getPath()));
            return new FileInputStream(config);
        }else {
            return new FileInputStream(config);
        }
    }

    public InputStream checkLanguageFile() throws IOException{
        if (!language.exists()){
            folder.mkdirs();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("language/language.yml");
            assert inputStream != null;
            Files.copy(inputStream, Paths.get(language.getPath()));
            return new FileInputStream(language);
        }else {
            return new FileInputStream(language);
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
    
    public boolean writeFile(String hash){
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(data, false);
            myWriter.write(hash);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                myWriter.close();
                isFileNotEmpty();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
}
