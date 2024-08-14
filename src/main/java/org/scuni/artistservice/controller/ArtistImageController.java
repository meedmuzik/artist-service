package org.scuni.artistservice.controller;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.service.ArtistService;
import org.scuni.artistservice.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/v1/artis/image/")
@RequiredArgsConstructor
public class ArtistImageController {
    private final ImageService imageService;
    private final ArtistService artistService;

    @PostMapping("{id}")
    public ResponseEntity<Object> uploadImage(@PathVariable("id") Integer id, MultipartFile image) {
        String imageFilename = imageService.upload(image);
        artistService.updateImageFilenameByImageFilename(imageFilename, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "image was created"));
    }
}
