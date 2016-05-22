/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freelancertech.poc.itext.utils;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

import com.google.common.io.ByteStreams;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author SimonPascal
 */

public class MyFileUtils {

    private static byte[] fileStreamToGson(String filePath) throws IOException {
        final ClassLoader classLoader = MyFileUtils.class.getClassLoader();
        return ByteStreams.toByteArray(classLoader.getResourceAsStream(filePath));
    }

    public static String generateBase64Image(String filePath) {
        
        try {
            return Base64.encodeBase64String(fileStreamToGson(filePath));
        } catch (IOException ex) {
            Logger.getLogger(MyFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
         return "";
       
    }
}
