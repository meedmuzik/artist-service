package org.scuni.artistservice.controller;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.service.ArtistService;
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
        String imagesUrl = "/api/v1/artis/image/" + artist.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "artist", artist,
                        "imagesUrl", imagesUrl
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getArtistById(@PathVariable Integer id){

        artistService.getArtistById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of());
    }
}
