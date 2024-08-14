package org.scuni.artistservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.entity.Artist;
import org.scuni.artistservice.mapper.ArtistCreateMapper;
import org.scuni.artistservice.mapper.ArtistReadDtoMapper;
import org.scuni.artistservice.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistCreateMapper artistCreateMapper;
    private final ArtistReadDtoMapper artistReadDtoMapper;

    public ArtistReadDto createArtist(ArtistCreateDto artistCreateDto) {
        Artist artist = artistCreateMapper.map(artistCreateDto);
        artistRepository.saveAndFlush(artist);
        log.info("Artist after creating, object: {}", artist);
        return artistReadDtoMapper.map(artist);
    }

    public void updateImageFilenameByImageFilename(String filename, Integer id){
        artistRepository.updateImageFilenameById(filename, id);
    }

    public void getArtistById(Integer id) {
        Optional<Artist> artist = artistRepository.findById(id);
    }
}
