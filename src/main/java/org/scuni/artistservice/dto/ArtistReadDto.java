package org.scuni.artistservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ArtistReadDto {
    Integer id;
    String nickname;
    List<TrackDto> tracks;
    String imageUrl;
}
