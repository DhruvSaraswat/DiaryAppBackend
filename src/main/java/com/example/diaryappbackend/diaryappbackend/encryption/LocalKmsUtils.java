package com.example.diaryappbackend.diaryappbackend.encryption;

import java.io.*;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class LocalKmsUtils {

    public static byte[] createMasterKey(String path) {
        byte[] masterKey = new byte[96];
        new SecureRandom().nextBytes(masterKey);

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            fileOutputStream.write(masterKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return masterKey;
    }

    public static byte[] readMasterKey(String path) {
        byte[] masterKey = new byte[96];

        try (FileInputStream stream = new FileInputStream(path)) {
            stream.read(masterKey, 0, 96);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return masterKey;
    }

    public static Map<String, Map<String, Object>> providersMap(String masterKeyPath) {
        File masterKeyFile = new File(masterKeyPath);
        byte[] masterKey = masterKeyFile.isFile() ? readMasterKey(masterKeyPath) : createMasterKey(masterKeyPath);

        Map<String, Object> masterKeyMap = new HashMap<>();
        masterKeyMap.put("key", masterKey);
        Map<String, Map<String, Object>> providersMap = new HashMap<>();
        providersMap.put("local", masterKeyMap);
        return providersMap;
    }
}
