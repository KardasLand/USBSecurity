/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.ui;

import com.kardasland.security.usbsecurity.Launcher;
import static com.kardasland.security.usbsecurity.Launcher.fileUtils;
import com.kardasland.security.usbsecurity.listener.USBListener;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ItemEvent;
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
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();
            Image icon = ImageIO.read(getClass().getResource("/images/static.png"));
            final TrayIcon trayIcon
                    = new TrayIcon(icon);
            final java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

            MenuItem aboutItem = new MenuItem(Launcher.language.systemTray.about);
            Menu displayMenu = new Menu(Launcher.language.systemTray.lockState);
            CheckboxMenuItem acikItem = new CheckboxMenuItem(Launcher.language.systemTray.onTitle);
            CheckboxMenuItem kapaliItem = new CheckboxMenuItem(Launcher.language.systemTray.offTitle);
            MenuItem exitItem = new MenuItem(Launcher.language.systemTray.quit);

            popup.add(aboutItem);
            popup.addSeparator();
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

            trayIcon.addActionListener(e -> JOptionPane.showMessageDialog(null, "Anti-USB Software 1.0\nDeveloped by KardasLand", "Anti USB", JOptionPane.INFORMATION_MESSAGE));

            aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Anti-USB Software 1.0\nDeveloped by KardasLand", "Anti USB", JOptionPane.INFORMATION_MESSAGE));

            acikItem.addItemListener(e -> {
                int changed = e.getStateChange();
                if (changed == ItemEvent.SELECTED) {
                    kapaliItem.setState(false);
                    changeState(true);
                } else {
                    boolean i = kapaliItem.getState();
                    if (!i) {
                        acikItem.setState(true);
                    }
                }
            });

            kapaliItem.addItemListener(e -> {
                int changed = e.getStateChange();
                if (changed == ItemEvent.SELECTED) {
                    acikItem.setState(false);
                    changeState(false);
                } else {
                    boolean i = acikItem.getState();
                    if (!i) {
                        kapaliItem.setState(true);
                    }
                }
            });

            exitItem.addActionListener(e -> {
                tray.remove(trayIcon);
                System.exit(0);
            });

            boolean active = Launcher.configManager.isActive();
            usbListener = new USBListener(active);
            (active ? acikItem : kapaliItem).setState(true);

            usbListener.start();
        } catch (IOException ex) {
            Logger.getLogger(SystemTray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeState(boolean state) {
        if (state) {
            JOptionPane.showMessageDialog(null, Launcher.language.lock.onMessage, Launcher.language.panelTitles.success, JOptionPane.INFORMATION_MESSAGE);
            usbListener.setEnabled(true);
            Launcher.configManager.set("lock", true);
            Launcher.configManager.save();
        } else {
            String password = JOptionPane.showInputDialog(null, Launcher.language.lock.enterPassword, Launcher.language.lock.enterPasswordTitle, JOptionPane.PLAIN_MESSAGE);
            if (Launcher.encryptionUtils.isCorrect(password, fileUtils.getHash())) {
                usbListener.setEnabled(false);
                JOptionPane.showMessageDialog(null, Launcher.language.lock.offMessage, Launcher.language.panelTitles.success, JOptionPane.INFORMATION_MESSAGE);
                Launcher.configManager.set("lock", false);
                Launcher.configManager.save();
            }else {
                JOptionPane.showMessageDialog(null, Launcher.language.login.wrong, Launcher.language.panelTitles.error, JOptionPane.ERROR_MESSAGE);
                changeState(state);
            }
        }
    }
}
