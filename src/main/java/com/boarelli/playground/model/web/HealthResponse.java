package com.boarelli.playground.model.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderClassName = "HealthResponseBuilder")
@JsonDeserialize(builder = HealthResponse.HealthResponseBuilder.class)
@AllArgsConstructor
public class HealthResponse {

    private String message;

}
