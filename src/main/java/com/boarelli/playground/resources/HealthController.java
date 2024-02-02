package com.boarelli.playground.resources;

import com.boarelli.playground.model.web.HealthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> getHealth() {
        return new ResponseEntity<>(HealthResponse.builder().message("service is healthy").build(), HttpStatus.OK);
    }

}
