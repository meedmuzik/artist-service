package org.scuni.artistservice.dto;

import lombok.Data;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArtistCreateDto {
    String nickname;
}
