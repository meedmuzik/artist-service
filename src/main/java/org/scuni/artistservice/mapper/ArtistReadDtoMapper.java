package org.scuni.artistservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistReadDtoMapper implements Mapper<Artist, ArtistReadDto> {
    @Override
    public ArtistReadDto map(Artist object) {
        return ArtistReadDto.builder()
                .id(object.getId())
                .nickname(object.getNickname())
                .build();
    }


}
