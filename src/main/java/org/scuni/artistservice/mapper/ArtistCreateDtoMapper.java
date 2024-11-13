package org.scuni.artistservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateEditDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistCreateDtoMapper implements Mapper<ArtistCreateEditDto, Artist> {

    @Override
    public Artist map(ArtistCreateEditDto object) {
        Artist artist = new Artist();
        artist.setNickname(object.getNickname());
        return artist;
    }
}