package com.boarelli.playground.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UrlShortenerTest {

    private UrlShortener urlShortener;

    @BeforeEach
    void init() throws NoSuchAlgorithmException {
        urlShortener = new UrlShortener();
    }

    @DisplayName("generate base64 hash short value")
    @Test
    void convertUrlToBase64Hash() {
        var convertedUrl = urlShortener.generateHashFromUrl("https://my-domain.com/with-super-loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong-path");
        assertNotNull(convertedUrl);
    }

}
