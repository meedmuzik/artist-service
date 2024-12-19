package org.scuni.artistservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
@Builder
public class ArtistReadDto {
    Long id;
    String nickname;
    String imageUrl;
    Double rating;
    List<Long> trackIds;
    List<TrackDto> tracks;
    Pageable pageable;
}