package org.scuni.artistservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistCreateEditDto {
    @NotBlank
    String nickname;
}
