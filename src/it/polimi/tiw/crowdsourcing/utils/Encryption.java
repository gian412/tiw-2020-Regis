package it.polimi.tiw.crowdsourcing.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Encryption {

    private final static char[] hexArray = "0F1E2D3C4B5A6978".toCharArray();
    private final static String salt = "051423";

    private Encryption() {
    }

    private static String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int i = 0; i < bytes.length; i++) {
            v = bytes[i] & 0xFF;
            hexChars[i*2] = hexArray[v >>> 4];
            hexChars[i*2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);

    }

    public static String hashString(String password) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            messageDigest.update(salt.getBytes());

            byte[] hashedPassword = messageDigest.digest();
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }

}
