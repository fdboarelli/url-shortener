package com.boarelli.playground.service;

import com.boarelli.playground.components.UrlShortener;
import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.entities.UrlEntity;
import com.boarelli.playground.model.errors.UrlAlreadyExistsException;
import com.boarelli.playground.model.errors.UrlNotFoundException;
import com.boarelli.playground.model.mapper.UrlMapper;
import com.boarelli.playground.model.mapper.UrlMapperImpl;
import com.boarelli.playground.repository.UrlDTORepository;
import com.boarelli.playground.repository.UrlEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

    private UrlShortenerService urlShortenerService;
    private UrlDTORepository urlDTORepository;
    private UrlEntityRepository urlEntityRepository;
    @Spy
    private UrlMapper urlMapper = new UrlMapperImpl();


    @BeforeEach
    void init() throws NoSuchAlgorithmException {
        var urlShortener = new UrlShortener();
        urlEntityRepository = mock(UrlEntityRepository.class);
        urlDTORepository = mock(UrlDTORepository.class);
        urlShortenerService = new UrlShortenerService("https://test-url.com/", urlShortener, urlMapper,
                urlEntityRepository, urlDTORepository);
    }

    @DisplayName("create new url")
    @Test
    void createNewCompressedUrl() {
        when(urlEntityRepository.save(any(UrlEntity.class))).thenReturn(UrlEntity.builder().build());
        when(urlMapper.urlEntityToDto(any(UrlEntity.class))).thenReturn(new UrlDTO());
        var response = urlShortenerService.createCompressedUrl("https://my-long-url.com");
        verify(urlEntityRepository, times(1)).save(any(UrlEntity.class));
        verify(urlDTORepository, times(1)).save(any(UrlDTO.class));
        assertNotNull(response);
        String compressedUrlPattern = "https://test-url\\.com/[a-zA-Z0-9]{9,}$";
        assertTrue(response.getUrl().matches(compressedUrlPattern));
    }

    @DisplayName("create new url - already exists in cache exception")
    @Test
    void createNewCompressedUrlAlreadyExistInCachetException() {
        when(urlDTORepository.findByShortUrl(anyString())).thenReturn(Optional.of(new UrlDTO()));
        assertThrows(UrlAlreadyExistsException.class, () -> urlShortenerService.createCompressedUrl("https://my-long-url.com"));
        verify(urlEntityRepository, never()).findByShortUrl(anyString());
        verify(urlEntityRepository, never()).save(any(UrlEntity.class));
        verify(urlDTORepository, never()).save(any(UrlDTO.class));
    }

    @DisplayName("create new url - already exists exception")
    @Test
    void createNewCompressedUrlAlreadyExistException() {
        when(urlDTORepository.findByShortUrl(anyString())).thenReturn(Optional.empty());
        when(urlEntityRepository.findByShortUrl(anyString())).thenReturn(Optional.of(new UrlEntity()));
        assertThrows(UrlAlreadyExistsException.class, () -> urlShortenerService.createCompressedUrl("https://my-long-url.com"));
        verify(urlEntityRepository, never()).save(any(UrlEntity.class));
        verify(urlDTORepository, never()).save(any(UrlDTO.class));
    }

    @DisplayName("get all urls")
    @Test
    void getAllUrl() {
        var urlsList = List.of(UrlEntity.builder().originalUrl("https://my-loooooooooooooong-url.com")
                .shortUrl("https://test-url.com/short").build(), UrlEntity.builder().originalUrl("https://my-loooooooooooooong-url2.com")
                .shortUrl("https://test-url.com/short2").build());
        when(urlEntityRepository.findAll()).thenReturn(urlsList);
        var urlDtos = urlShortenerService.getAllUrls();
        assertEquals(2, urlDtos.size());
        assertEquals("https://test-url.com/short", urlDtos.getFirst().getShortUrl());
        assertEquals("https://my-loooooooooooooong-url.com", urlDtos.getFirst().getOriginalUrl());
        assertEquals("https://test-url.com/short2", urlDtos.get(1).getShortUrl());
        assertEquals("https://my-loooooooooooooong-url2.com", urlDtos.get(1).getOriginalUrl());
    }

    @DisplayName("get cached url")
    @Test
    void getCachedUrl() {
        var url = UrlDTO.builder().originalUrl("https://my-loooooooooooooong-url.com")
                .shortUrl("https://test-url.com/short").build();
        when(urlDTORepository.findById(any(UUID.class))).thenReturn(Optional.of(url));
        var urlDto = urlShortenerService.getUrl(UUID.randomUUID().toString());
        assertEquals("https://test-url.com/short", urlDto.getShortUrl());
        assertEquals("https://my-loooooooooooooong-url.com", urlDto.getOriginalUrl());
        verify(urlEntityRepository, never()).findById(any(UUID.class));
    }

    @DisplayName("get url")
    @Test
    void getUrl() {
        when(urlDTORepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        var url = UrlEntity.builder().originalUrl("https://my-loooooooooooooong-url.com")
                .shortUrl("https://test-url.com/short").build();
        when(urlEntityRepository.findById(any(UUID.class))).thenReturn(Optional.of(url));
        var urlDto = urlShortenerService.getUrl(UUID.randomUUID().toString());
        assertEquals("https://test-url.com/short", urlDto.getShortUrl());
        assertEquals("https://my-loooooooooooooong-url.com", urlDto.getOriginalUrl());
        verify(urlDTORepository, times(1)).findById(any(UUID.class));
    }

    @DisplayName("get url - url not found exception")
    @Test
    void getUrlNotFoundException() {
        when(urlDTORepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(urlEntityRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(UrlNotFoundException.class, () -> urlShortenerService.getUrl(UUID.randomUUID().toString()));
    }

    @DisplayName("delete url")
    @Test
    void deleteUrl() {
        urlShortenerService.deleteUrl(UUID.randomUUID().toString());
        verify(urlDTORepository, times(1)).deleteById(any(UUID.class));
        verify(urlEntityRepository, times(1)).deleteById(any(UUID.class));
    }

    @DisplayName("delete url - url not found exception")
    @Test
    void deleteUrlNotFoundException() {
        doThrow(UrlNotFoundException.class).when(urlEntityRepository).deleteById(any(UUID.class));
        assertThrows(UrlNotFoundException.class, () -> urlShortenerService.deleteUrl(UUID.randomUUID().toString()));
        verify(urlEntityRepository, times(1)).deleteById(any(UUID.class));
    }

}
