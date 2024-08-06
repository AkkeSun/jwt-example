package com.sweettracker.authserver.global.util;

public interface AesUtil {

    String encryptText(String plainText);

    String decryptText(String encryptedText);

    boolean matches(String plainText, String encryptedText);
}