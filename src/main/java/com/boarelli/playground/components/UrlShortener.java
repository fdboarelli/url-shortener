package com.boarelli.playground.components;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class UrlShortener {
    private final MessageDigest digest;

    public UrlShortener() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA3-256");
    }

    public String generateHashFromUrl(String url) {
        return DigestUtils.sha3_256Hex(digest.digest(url.getBytes(StandardCharsets.UTF_8))).substring(0, 9);
    }

}
