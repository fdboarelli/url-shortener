package com.boarelli.playground.repository;

import com.boarelli.playground.model.entities.UrlEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UrlEntityRepository extends CrudRepository<UrlEntity, UUID> {

    Optional<UrlEntity> findByShortUrl(String shortUrl);

}
