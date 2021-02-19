/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity;

import com.kardasland.security.usbsecurity.ui.SystemTray;
import com.kardasland.security.usbsecurity.utils.EncryptionUtils;
import com.kardasland.security.usbsecurity.utils.FileUtils;
import com.kardasland.security.usbsecurity.utils.USBListener;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JOptionPane;

/**
 *
 * @author karda
 */
public class Launcher {
    
    public static Launcher instance;
    public static EncryptionUtils encryptionUtils = new EncryptionUtils();
    public static FileUtils fileUtils = new FileUtils();
    
    public static void main(String[] args) throws IOException {
        //new MainMenu().setVisible(true);
        
        if (fileUtils.checkFile() && fileUtils.isFileNotEmpty()) {
            loginPane();
        }else {
            registerPane();
        }
    }
    public static void loginPane(){
        String password = JOptionPane.showInputDialog(null, "Giriş için şifrenizi girin:", "Giriş", JOptionPane.PLAIN_MESSAGE);
        if (encryptionUtils.isCorrect(password, fileUtils.getHash())) {
            new SystemTray().setSystemTray();
        } else {
            JOptionPane.showMessageDialog(null, "Şifre hatalı! Tekrar deneyin.", "Hatalı!", JOptionPane.ERROR_MESSAGE);
            loginPane();
        }
    }
    public static void registerPane(){
        String password = JOptionPane.showInputDialog(null, "Lütfen bir şifre belirleyin:", "Kayıt", JOptionPane.PLAIN_MESSAGE);
        if (!password.isEmpty()) {
            fileUtils.writeFile(encryptionUtils.encrypt(password));
            JOptionPane.showMessageDialog(null, "Başarıyla kaydettiniz! Bundan sonra bu şifreyi kullanacaksınız.", "Başarılı!", JOptionPane.INFORMATION_MESSAGE);
            new SystemTray().setSystemTray();
        } else {
            JOptionPane.showMessageDialog(null, "Şifre boş olamaz! Tekrar deneyin.", "Hatalı!", JOptionPane.ERROR_MESSAGE);
            registerPane();
        }
    }
}
