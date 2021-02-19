/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.ui;

import com.kardasland.security.usbsecurity.Launcher;
import static com.kardasland.security.usbsecurity.Launcher.fileUtils;
import com.kardasland.security.usbsecurity.utils.USBListener;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author karda
 */
public class SystemTray {
    
    public USBListener usbListener;
    public void setSystemTray() {
        try {
            //Check the SystemTray support
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();
            Image icon = ImageIO.read(getClass().getResource("/images/static.png"));
            final TrayIcon trayIcon
                    = new TrayIcon(icon);
            final java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

            // Create a popup menu components
            MenuItem aboutItem = new MenuItem("Hakkında");
            //MenuItem cb1 = new MenuItem("Menüyü Aç");
            //CheckboxMenuItem cb2 = new CheckboxMenuItem("Şifre Değiştir");
            Menu displayMenu = new Menu("Kilit Durumu");
            CheckboxMenuItem acikItem = new CheckboxMenuItem("Açık");
            CheckboxMenuItem kapaliItem = new CheckboxMenuItem("Kapalı");
            MenuItem exitItem = new MenuItem("Çıkış");

            //Add components to popup menu
            popup.add(aboutItem);
            popup.addSeparator();
            //popup.add(cb1);
            //popup.add(cb2);
            popup.add(displayMenu);
            displayMenu.add(acikItem);
            displayMenu.add(kapaliItem);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
                return;
            }

            trayIcon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Anti-USB Software 1.0\nDeveloped by KardasLand", "Anti USB", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            aboutItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Anti-USB Software 1.0\nDeveloped by KardasLand", "Anti USB", JOptionPane.INFORMATION_MESSAGE);

                    //new MainMenu().setVisible(true);
                }
            });

            //trayIcon.displayMessage("Sun TrayIcon Demo","This is a warning message", TrayIcon.MessageType.WARNING );
            acikItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    int changed = e.getStateChange();
                    if (changed == ItemEvent.SELECTED) {
                        kapaliItem.setState(false);
                        changeState(true);
                    } else {
                        boolean i = kapaliItem.getState();
                        if (i == false) {
                            acikItem.setState(true);
                        }
                    }
                }

            });
            kapaliItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    int changed = e.getStateChange();
                    if (changed == ItemEvent.SELECTED) {
                        acikItem.setState(false);
                        changeState(false);
                    } else {
                        boolean i = acikItem.getState();
                        if (i == false) {
                            kapaliItem.setState(true);
                        }
                    }
                }

            });
            /*
            cb1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
             */

            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);
                    System.exit(0);
                }
            });
            
            if (Launcher.fileUtils.getActive() == 1) {
                usbListener = new USBListener(true);
                acikItem.setState(true);
            }else {
                usbListener = new USBListener(false);
                kapaliItem.setState(true);
            }
            usbListener.start();
        } catch (IOException ex) {
            Logger.getLogger(SystemTray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeState(boolean state) {
        if (state) {
            //kilit açıldı
            JOptionPane.showMessageDialog(null, "Başarıyla kilidi açtın.", "Başarılı!", JOptionPane.INFORMATION_MESSAGE);
            usbListener.setEnabled(true);
            fileUtils.changeActiveState(1);
        } else {
            changeStateLogin();
        }
    }
    
    public void changeStateLogin() {
        String password = JOptionPane.showInputDialog(null, "Kapatmak için şifrenizi girin:", "Kilidi Kapatma", JOptionPane.PLAIN_MESSAGE);
        if (Launcher.encryptionUtils.isCorrect(password, fileUtils.getHash())) {
            usbListener.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Başarıyla kilidi kapattın.", "Başarılı!", JOptionPane.INFORMATION_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, "Şifre hatalı! Tekrar deneyin.", "Hatalı!", JOptionPane.ERROR_MESSAGE);
            changeStateLogin();
            fileUtils.changeActiveState(0);
        }
    }
}
