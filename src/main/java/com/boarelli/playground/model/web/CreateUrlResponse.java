package com.boarelli.playground.model.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(builderClassName = "CreateUrlResponseBuilder")
@JsonDeserialize(builder = CreateUrlResponse.CreateUrlResponseBuilder.class)
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlResponse {
    private String url;
}
