/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity;

import com.kardasland.security.usbsecurity.ui.SystemTray;
import com.kardasland.security.usbsecurity.utils.EncryptionUtils;
import com.kardasland.security.usbsecurity.utils.file.FileUtils;
import com.kardasland.security.usbsecurity.utils.file.Config;
import com.kardasland.security.usbsecurity.utils.language.Language;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author karda
 */
public class Launcher {
    public static EncryptionUtils encryptionUtils = new EncryptionUtils();
    public static FileUtils fileUtils = new FileUtils();
    public static Config configManager;
    public static Language language;

    public static void main(String[] args) throws IOException {

        configManager = new Config(fileUtils.checkConfigFile(), fileUtils.config);
        configManager.load();


        Yaml yaml = new Yaml(new Constructor(Language.class));
        language = yaml.load(fileUtils.checkLanguageFile());


        if (fileUtils.checkDataFile() && fileUtils.isFileNotEmpty()) {
            loginPane();
        }else {
            registerPane();
        }
    }

    public static void loginPane(){
        String password = JOptionPane.showInputDialog(null, language.login.message, language.login.title, JOptionPane.PLAIN_MESSAGE);
        if (encryptionUtils.isCorrect(password, fileUtils.getHash())) {
            new SystemTray().setSystemTray();
        } else {
            JOptionPane.showMessageDialog(null, language.login.wrong, language.panelTitles.error, JOptionPane.ERROR_MESSAGE);
            loginPane();
        }
    }

    public static void registerPane(){
        String password = JOptionPane.showInputDialog(null, language.register.message, language.register.title, JOptionPane.PLAIN_MESSAGE);
        if (!password.isEmpty()) {
            fileUtils.writeFile(encryptionUtils.encrypt(password));
            JOptionPane.showMessageDialog(null, language.register.success, language.panelTitles.success, JOptionPane.INFORMATION_MESSAGE);
            new SystemTray().setSystemTray();
        } else {
            JOptionPane.showMessageDialog(null, language.register.empty, language.panelTitles.error, JOptionPane.ERROR_MESSAGE);
            registerPane();
        }
    }
}
