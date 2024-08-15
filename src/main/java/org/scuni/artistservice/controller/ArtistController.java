package org.scuni.artistservice.controller;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.service.ArtistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<Object> createArtist(@RequestBody ArtistCreateDto artistCreateDto) {
        ArtistReadDto artist = artistService.createArtist(artistCreateDto);
        String imageUrl = "/api/v1/artist/image/" + artist.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "artist", artist,
                        "imageUrl", imageUrl
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistReadDto> getArtistById(@PathVariable Integer id) {
        ArtistReadDto artist = artistService.getArtistById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artist);
    }

    @GetMapping()
    public ResponseEntity<Page<ArtistSearchReadDto>> getArtistByNicknameWithPagination(Pageable pageable, @RequestParam String nickname) {
        Page<ArtistSearchReadDto> artists = artistService.getArtistByNickname(nickname, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(artists);
    }
}
