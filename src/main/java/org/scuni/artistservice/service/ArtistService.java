package org.scuni.artistservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.entity.Artist;
import org.scuni.artistservice.exception.ArtistNotFoundException;
import org.scuni.artistservice.mapper.ArtistCreateMapper;
import org.scuni.artistservice.mapper.ArtistReadDtoMapper;
import org.scuni.artistservice.mapper.ArtistSearchReadDtoMapper;
import org.scuni.artistservice.repository.ArtistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistCreateMapper artistCreateMapper;
    private final ArtistReadDtoMapper artistReadDtoMapper;
    private final ArtistSearchReadDtoMapper artistSearchReadDtoMapper;

    public ArtistReadDto createArtist(ArtistCreateDto artistCreateDto) {
        Artist artist = artistCreateMapper.map(artistCreateDto);
        artistRepository.saveAndFlush(artist);
        log.info("Artist after creating, object: {}", artist);
        return artistReadDtoMapper.map(artist);
    }

    public void updateImageFilenameByImageFilename(String filename, Integer id) {
        artistRepository.updateImageFilenameById(filename, id);
    }

    @Transactional(readOnly = true)
    public ArtistReadDto getArtistById(Integer id) {
        return artistRepository.findById(id)
                .map(artist -> {
                    List<Integer> tracksId = artistRepository.findTracksIdByArtistId(id);
                    log.info("tracks id: {} for artist: {}", tracksId, artist);
                    return artistReadDtoMapper.map(artist);
                })
                .orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
    }

    @Transactional(readOnly = true)
    public Page<ArtistSearchReadDto> getArtistByNickname(String nickname, Pageable pageable) {
        Page<Artist> artists = artistRepository.findByNickname(pageable, nickname);
        return artists.map(artistSearchReadDtoMapper::map);
    }
}
