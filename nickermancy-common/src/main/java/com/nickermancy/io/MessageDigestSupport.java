package com.nickermancy.io;

import com.nickermancy.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.val;

public class MessageDigestSupport {

    private static final Logger log = Logger.getLogger();
    private static final Map<String, MessageDigest> digestCache = new ConcurrentHashMap<>();

    public static String md5(Path path) {
        return digest("MD5", path);
    }

    public static String sha1(Path path) {
        return digest("SHA-1", path);
    }

    public static String sha256(Path path) {
        return digest("SHA-256", path);
    }

    public static String md5(InputStream inputStream) {
        return digest("MD5", inputStream);
    }

    public static String sha1(InputStream inputStream) {
        return digest("SHA-1", inputStream);
    }

    public static String sha256(InputStream inputStream) {
        return digest("SHA-256", inputStream);
    }

    public static String digest(String algorithm, Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return digest(algorithm, in);
        } catch (IOException ex) {
            throw new MessageDigestException("Failed to compute digest: " + ex.getMessage(), ex);
        }
    }

    public static String digest(String algorithm, InputStream inputStream) {
        val digest = cloneCachedInstance(algorithm);
        val buffer = new byte[8192];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (IOException ex) {
            throw new MessageDigestException("Failed to compute digest: " + ex.getMessage(), ex);
        }
        return hexEncode(digest.digest());
    }

    private static String hexEncode(byte[] bytes) {
        val sb = new StringBuilder(bytes.length);
        for (val b : bytes) {
            val s = Integer.toHexString(Byte.toUnsignedInt(b));
            if (s.length() == 1) sb.append("0");
            sb.append(s);
        }
        return sb.toString();
    }

    private static MessageDigest getInstance(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new MessageDigestException("No such algorithm: " + algorithm, ex);
        }
    }

    private static MessageDigest cloneCachedInstance(String algorithm) {
        MessageDigest digest = digestCache.computeIfAbsent(algorithm, MessageDigestSupport::getInstance);
        if (digest instanceof Cloneable) {
            try {
                digest = (MessageDigest) digest.clone();
            } catch (CloneNotSupportedException ex) {
                // Shouldn't get here, because we already made sure it was cloneable
                digest = getInstance(algorithm);
                log.debug(() -> "Message digest for " + algorithm + " says it is cloneable, but it isn't.", ex);
            }
        } else {
            digest = getInstance(algorithm);
        }
        return digest;
    }
}
