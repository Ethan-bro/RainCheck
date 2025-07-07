package main.java.data_access;

import main.java.use_case.login.PasswordHashingService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Secure implementation of password hashing using SHA-256.
 */
public class SHA256HashingService implements PasswordHashingService {

    @Override
    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashed) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }
}