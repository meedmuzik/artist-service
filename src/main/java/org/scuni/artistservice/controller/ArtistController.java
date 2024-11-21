package org.scuni.artistservice.controller;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateEditDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.dto.QueryDto;
import org.scuni.artistservice.service.ArtistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping("/artist")
    public ResponseEntity<Object> createArtist(@RequestBody @Validated ArtistCreateEditDto artistCreateEditDto) {
        ArtistReadDto artist = artistService.createArtist(artistCreateEditDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("artistId", artist.getId(),
                        "imageUrl", "/api/v1/images/artist/" + artist.getId(),
                        "message", "Artist was added"));
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<ArtistReadDto> getArtistById(@PathVariable("id") Long id, Pageable pageable) {
        ArtistReadDto artist = artistService.getArtistById(id, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artist);
    }

    @GetMapping("/artists")
    public ResponseEntity<Page<ArtistSearchReadDto>> getArtistsByNicknameWithPagination(@RequestParam("nickname") String nickname, Pageable pageable) {
        Page<ArtistSearchReadDto> artists = artistService.getArtistByNickname(nickname, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artists);
    }

    @PostMapping("/artist/{id}")
    public ResponseEntity<Object> updateArtistById(@PathVariable("id") Long id, @RequestBody @Validated ArtistCreateEditDto artistCreateEditDto) {
        ArtistReadDto artistReadDto = artistService.updateArtistById(id, artistCreateEditDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("artist", artistReadDto));
    }

    @DeleteMapping("/artist/{id}")
    public ResponseEntity<Object> deleteArtistById(@PathVariable("id") Long id) {
        artistService.deleteArtistById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/artist/{artistId}/track/{trackId}")
    public ResponseEntity<Object> addTrackToArtist(@PathVariable("artistId") Long artistId, @PathVariable("trackId") Long trackId) {
        artistService.addTrackToArtist(artistId, trackId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "track was added to artist"));
    }

    @PostMapping("/recommendations/artists")
    public ResponseEntity<Object> getRecommendedArtists(@RequestBody QueryDto query) {
        List<ArtistReadDto> recommendedArtists = artistService.getRecommendedArtists(query);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("artists", recommendedArtists));
    }
}
