package com.boarelli.playground;

import com.boarelli.playground.helper.IntegrationTest;
import com.boarelli.playground.resources.UrlShortenerController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UrlShortenerApplicationTests extends IntegrationTest {

	private final UrlShortenerController urlShortenerController;
	@Autowired
	UrlShortenerApplicationTests(UrlShortenerController urlShortenerController) {
		this.urlShortenerController = urlShortenerController;
	}

	@DisplayName("context load - ok")
	@Test
	void contextLoads() {
		assertThat(urlShortenerController).isNotNull();
	}

}
