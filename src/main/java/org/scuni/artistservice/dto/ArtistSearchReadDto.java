package org.scuni.artistservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistSearchReadDto {
    Long id;
    String nickname;
    String imageUrl;
}
