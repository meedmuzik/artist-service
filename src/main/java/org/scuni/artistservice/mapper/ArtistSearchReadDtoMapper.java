package org.scuni.artistservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistSearchReadDtoMapper implements Mapper<Artist, ArtistSearchReadDto> {
    private static final String IMAGE_URL_PREFIX = "/api/v1/images/artist/";

    @Override
    public ArtistSearchReadDto map(Artist object) {
        return ArtistSearchReadDto.builder()
                .id(object.getId())
                .nickname(object.getNickname())
                .imageUrl(IMAGE_URL_PREFIX + object.getImageFilename())
                .build();
    }
}
