package com.boarelli.playground.resources;

import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.web.CreateUrlRequest;
import com.boarelli.playground.model.web.CreateUrlResponse;
import com.boarelli.playground.service.UrlShortenerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/v1/urls")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping
    @ResponseBody
    public List<UrlDTO> getAllUrl() {
        log.info("Received get all urls request, served on {}", Thread.currentThread());
        return urlShortenerService.getAllUrls();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public UrlDTO getUrlByCompressedUrl(@PathVariable("id") String urlId) {
        log.info("Received get url request for id {}, served on {}", urlId, Thread.currentThread());
        return urlShortenerService.getUrl(urlId);
    }

    @PostMapping
    @ResponseBody
    public CreateUrlResponse createUrl(@RequestBody CreateUrlRequest request) {
        log.info("Received create url request {}, served on {}", request, Thread.currentThread());
        return urlShortenerService.createCompressedUrl(request.getUrl());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUrl(@PathVariable("id") String urlId) {
        log.info("Received delete url request for id {}, served on {}", urlId, Thread.currentThread());
        urlShortenerService.deleteUrl(urlId);
    }

}
