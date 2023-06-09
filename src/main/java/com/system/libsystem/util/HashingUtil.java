package com.system.libsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HashingUtil {

    private static final String SHA_256_ALGORITHM = "SHA-256";
    private static final String HEX_FORMAT = "%02x";

    public static String hashData(String data) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            final byte[] hashedBytes = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = String.format(HEX_FORMAT, b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    public static boolean compareRawAndHashedData(String rawData, String storedHashedData) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            final byte[] hashedBytes = messageDigest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = String.format(HEX_FORMAT, b);
                hexString.append(hex);
            }
            String hashedResult = hexString.toString();
            return hashedResult.equals(storedHashedData);
        } catch (NoSuchAlgorithmException exception) {
            log.error(exception.getMessage());
        }
        return false;
    }

}
