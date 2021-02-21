/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kardasland.security.usbsecurity.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kardasland.security.usbsecurity.Launcher;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author karda
 */
public class EncryptionUtils {
    public String encrypt(String password) {
        return BCrypt.withDefaults().hashToString(16, password.toCharArray());
    }
    public boolean isCorrect(String password, String hash) {
            try {
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
                return result.verified;
            }catch (NullPointerException ex){
                System.exit(-1);
                return false;
            }
    }
    
}
