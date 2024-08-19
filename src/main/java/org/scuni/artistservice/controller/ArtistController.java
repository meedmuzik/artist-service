package org.scuni.artistservice.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateEditDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.service.ArtistService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final StreamBridge streamBridge;

    @PostMapping("/artist")
    public ResponseEntity<Object> createArtist(@RequestBody @Validated ArtistCreateEditDto artistCreateEditDto) {
        ArtistReadDto artist = artistService.createArtist(artistCreateEditDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("artistId", artist.getId(),
                        "imageUrl", "/api/v1/images/artist/" + artist.getId(),
                        "message", "Artist was added"));
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<ArtistReadDto> getArtistById(@PathVariable @Min(1) Integer id, Pageable pageable) {
        streamBridge.send("produce-out-0", "s");
        ArtistReadDto artist = artistService.getArtistById(id, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artist);
    }

    @GetMapping("/artists")
    public ResponseEntity<Page<ArtistSearchReadDto>> getArtistsByNicknameWithPagination(@RequestParam @NotBlank String nickname, Pageable pageable) {
        Page<ArtistSearchReadDto> artists = artistService.getArtistByNickname(nickname, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artists);
    }

    @PostMapping("/artist/{id}")
    public ResponseEntity<Object> updateArtistById(@PathVariable @Min(1) Integer id, @RequestBody @Validated ArtistCreateEditDto artistCreateEditDto) {
        ArtistReadDto artistReadDto = artistService.updateArtistById(id, artistCreateEditDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("artist", artistReadDto));
    }

    @DeleteMapping("/artist/{id}")
    public ResponseEntity<Object> deleteArtistById(@PathVariable @Min(1) Integer id) {
        artistService.deleteArtistById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/artist/{artistId}/track/{trackId}")
    public ResponseEntity<Object> addTrackToArtist(@PathVariable Integer artistId, @PathVariable @Min(1) Long trackId) {
        artistService.addTrackToArtist(artistId, trackId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "track was added to artist"));
    }
}
