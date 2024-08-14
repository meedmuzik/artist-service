package org.scuni.artistservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ArtistReadDto {
    Integer id;
    String nickname;
}
