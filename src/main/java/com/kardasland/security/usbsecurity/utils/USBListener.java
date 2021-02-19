/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.utils;

import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.USBStorageDevice;
import net.samuelcampos.usbdrivedetector.events.DeviceEventType;
import net.samuelcampos.usbdrivedetector.events.IUSBDriveListener;
import net.samuelcampos.usbdrivedetector.events.USBStorageEvent;

/**
 *
 * @author karda
 */
public class USBListener {
    private boolean enabled;
    USBDeviceDetectorManager driveDetector;
    
    public USBListener(boolean enabled){
        this.enabled = enabled;
        this.driveDetector = new USBDeviceDetectorManager();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void lockedMessage(){
        JOptionPane.showMessageDialog(null, "Cihaz USB girişlerine kilitlidir.", "Kilitli!", JOptionPane.ERROR_MESSAGE);
    }
    
    public void start() {
        driveDetector.addDriveListener(new IUSBDriveListener() {
            @Override
            public void usbDriveEvent(USBStorageEvent usbse) {
                if (enabled && usbse.getEventType().equals(DeviceEventType.CONNECTED)) {
                    try {
                        driveDetector.unmountStorageDevice(usbse.getStorageDevice());
                        lockedMessage();
                    } catch (IOException ex) {
                        Logger.getLogger(USBListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

}
