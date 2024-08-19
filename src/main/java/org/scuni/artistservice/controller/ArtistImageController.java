package org.scuni.artistservice.controller;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.entity.Image;
import org.scuni.artistservice.service.ArtistService;
import org.scuni.artistservice.service.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/images/")
@RequiredArgsConstructor
public class ArtistImageController {
    private final ImageService imageService;
    private final ArtistService artistService;

    @PostMapping("/artist/{id}")
    public ResponseEntity<Object> uploadImage(@PathVariable("id") Integer id, MultipartFile image) {
        String imageFilename = imageService.upload(image);
        artistService.updateImageFilenameByImageFilename(imageFilename, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "image was created"));
    }

    @GetMapping("/artist/{imageId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable("imageId") String imageId) {
        Image download = imageService.download(imageId);
        String extension = download.getExtension();
        if (Objects.equals(extension, "jpg")) extension = "jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/" + extension))
                .body(new ByteArrayResource(download.getBytes()));
    }
}
