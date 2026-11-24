package com.example.banji.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * BCrypt密码加密工具类（简化稳定版）
 * 使用 SHA-256 + 随机盐 实现密码加密
 */
public class BCrypt {

    private static final int SALT_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机盐值
     */
    public static String gensalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 加密密码（使用 SHA-256 + 盐）
     */
    public static String hashpw(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashed = md.digest(password.getBytes());
            // 返回格式: 盐$加密结果
            return salt + "$" + Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 加密密码（自动生成盐）
     */
    public static String hashpw(String password) {
        return hashpw(password, gensalt());
    }

    /**
     * 验证密码是否匹配
     */
    public static boolean checkpw(String plaintext, String hashed) {
        if (hashed == null || !hashed.contains("$")) {
            return false;
        }
        try {
            String[] parts = hashed.split("\\$");
            if (parts.length != 2) {
                return false;
            }
            String salt = parts[0];
            String expectedHash = parts[1];

            String actualHash = hashpw(plaintext, salt);
            String[] actualParts = actualHash.split("\\$");

            return expectedHash.equals(actualParts[1]);
        } catch (Exception e) {
            return false;
        }
    }
}