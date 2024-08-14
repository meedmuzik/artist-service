package org.scuni.artistservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistCreateMapper implements Mapper<ArtistCreateDto, Artist> {


    @Override
    public Artist map(ArtistCreateDto object) {
        Artist artist = new Artist();
        artist.setNickname(object.getNickname());
        return artist;
    }
}
