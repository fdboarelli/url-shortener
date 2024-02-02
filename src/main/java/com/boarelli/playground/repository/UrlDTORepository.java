package com.boarelli.playground.repository;


import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.entities.UrlEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlDTORepository extends CrudRepository<UrlDTO, UUID> {

    Optional<UrlDTO> findByShortUrl(String shortUrl);

}
