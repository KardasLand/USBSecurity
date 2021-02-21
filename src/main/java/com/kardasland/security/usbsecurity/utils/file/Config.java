package com.kardasland.security.usbsecurity.utils.file;

import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private Yaml yaml;
    private static Map<String, Object> list = new HashMap<>();
    private InputStream inputStream;
    private File file;

    public Config(InputStream inputStream, File file){
        yaml = new Yaml();
        this.inputStream = inputStream;
        this.file = file;
    }

    public void load(){
        list = yaml.load(inputStream);
    }

    public static Object get(String path){
        return list.get(path);
    }

    public boolean set(String path, Object object){
        list.put(path, object);
        return true;
    }

    public boolean save(){
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            yaml.dump(list, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An IO error occured. Could not saved state.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    public boolean isActive(){
        return (Boolean.parseBoolean(String.valueOf(list.get("lock"))));
    }


}
