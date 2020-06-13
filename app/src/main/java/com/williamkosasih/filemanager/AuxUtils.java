package com.williamkosasih.filemanager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuxUtils {
    static public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

    static public String resolveFileNameConflict(File src, File dst) {
        String baseName = src.getName().substring(0, src.getName().lastIndexOf('.'));
        String extension =
                src.getName().substring(src.getName().lastIndexOf('.'));
        int offset = 1;
        while (Files.exists(Paths.get(dst.getPath() +
                "/" + baseName + "(" + offset + ")" + extension))) {
            offset++;
        }
        return baseName + "(" + offset + ")" + extension;
    }
}
