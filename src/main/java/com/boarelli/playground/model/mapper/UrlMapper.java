package com.boarelli.playground.model.mapper;

import com.boarelli.playground.model.dtos.UrlDTO;
import com.boarelli.playground.model.entities.UrlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UrlMapper {

    UrlDTO urlEntityToDto(UrlEntity urlEntity);
    List<UrlDTO> urlEntitiesToDtos(List<UrlEntity> urlEntities);
    UrlEntity urlDtoToEntity(UrlDTO urlDTO);

}
