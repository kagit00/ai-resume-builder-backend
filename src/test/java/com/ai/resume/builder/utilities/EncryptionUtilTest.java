package com.ai.resume.builder.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EncryptionUtilTest {

    private static final String SECRET_KEY = "1234567890123456";
    private static final String SAMPLE_DATA = "This is a test message";

    @Test
    void encrypt_decrypt_shouldReturnOriginalData() {
        String encryptedData = EncryptionUtil.encrypt(SAMPLE_DATA, SECRET_KEY);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, SECRET_KEY);
        Assertions.assertEquals(SAMPLE_DATA, decryptedData, "Decrypted data with incorrect key should return an empty string");
    }

    @Test
    void decrypt_withIncorrectKey_shouldReturnEmptyString() {
        String encryptedData = EncryptionUtil.encrypt(SAMPLE_DATA, SECRET_KEY);
        String incorrectKey = "incorrectKey123456";
        String decryptedData = EncryptionUtil.decrypt(encryptedData, incorrectKey);
        Assertions.assertEquals("", decryptedData, "Decrypted data with incorrect key should return an empty string");
    }

    @Test
    void encrypt_withEmptyString_shouldReturnEncryptedString() {
        String emptyData = "";
        String encryptedData = EncryptionUtil.encrypt(emptyData, SECRET_KEY);
        assertFalse(encryptedData.isEmpty(), "Encrypted string should not be empty");
    }

    @Test
    void decrypt_withEmptyString_shouldReturnEmptyString() {
        String emptyData = "";
        String decryptedData = EncryptionUtil.decrypt(emptyData, SECRET_KEY);
        Assertions.assertEquals("", decryptedData, "string should not be empty");
    }

    @Test
    void decrypt_withInvalidEncryptedData_shouldReturnEmptyString() {
        String invalidEncryptedData = "invalid_encrypted_data";
        String decryptedData = EncryptionUtil.decrypt(invalidEncryptedData, SECRET_KEY);
        Assertions.assertEquals("", decryptedData, "Decrypted data should return empty string if data is invalid");
    }

    @Test
    void encrypt_decrypt_shouldHandleSpecialCharacters() {
        String specialCharData = "Special characters: !@#$%^&*()_+-=[]{}|;:,.<>?/~";
        String encryptedData = EncryptionUtil.encrypt(specialCharData, SECRET_KEY);
        String decryptedData = EncryptionUtil.decrypt(encryptedData, SECRET_KEY);
        Assertions.assertEquals(specialCharData, decryptedData, "Decrypted data with special characters should match the original");
    }
}