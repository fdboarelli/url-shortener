package com.boarelli.playground.model.errors;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UrlAlreadyExistsException extends RuntimeException {

    @NonNull
    private String message;

}
