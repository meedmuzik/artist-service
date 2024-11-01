package org.scuni.artistservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistReadDtoMapper implements Mapper<Artist, ArtistReadDto> {
    private static final String IMAGE_URL_PREFIX = "/api/v1/images/artist/";

    @Override
    public ArtistReadDto map(Artist object) {
        return ArtistReadDto.builder()
                .id(object.getId())
                .nickname(object.getNickname())
                .imageUrl(IMAGE_URL_PREFIX + object.getImageFilename())
                .trackIds(object.getTrackIds())
                .build();
    }
}
