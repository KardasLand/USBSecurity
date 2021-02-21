/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import com.kardasland.security.usbsecurity.Launcher;
import com.kardasland.security.usbsecurity.utils.file.Config;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.events.DeviceEventType;
import net.samuelcampos.usbdrivedetector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedetector.events.USBStorageEvent;

/**
 *
 * @author KardasLand
 */
public class USBListener {
    private boolean enabled;
    USBDeviceDetectorManager driveDetector;
    
    public USBListener(boolean enabled){
        this.enabled = enabled;
        this.driveDetector = new USBDeviceDetectorManager(Long.parseLong(String.valueOf(Config.get("checkInterval"))));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void lockedMessage(){
        JOptionPane.showMessageDialog(null, Launcher.language.lock.preventedUSB, Launcher.language.panelTitles.error, JOptionPane.ERROR_MESSAGE);
    }
    
    public void start() {
        driveDetector.addDriveListener(usbse -> {
            if (enabled && usbse.getEventType().equals(DeviceEventType.CONNECTED)) {
                try {
                    driveDetector.unmountStorageDevice(usbse.getStorageDevice());
                    lockedMessage();
                } catch (IOException ex) {
                    Logger.getLogger(USBListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
