package org.scuni.artistservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Data
public class TrackDto {
    private Long trackId;
    private String title;
    private String imageUrl;
    private String releaseDate;
    private Integer albumId;
    private String albumTitle;
}