package org.scuni.artistservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scuni.artistservice.dto.ArtistCreateEditDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.dto.TrackDto;
import org.scuni.artistservice.entity.Artist;
import org.scuni.artistservice.entity.TrackArtist;
import org.scuni.artistservice.exception.ArtistNotFoundException;
import org.scuni.artistservice.exception.TrackAlreadyExistException;
import org.scuni.artistservice.mapper.ArtistCreateDtoMapper;
import org.scuni.artistservice.mapper.ArtistReadDtoMapper;
import org.scuni.artistservice.mapper.ArtistSearchReadDtoMapper;
import org.scuni.artistservice.repository.ArtistRepository;
import org.scuni.artistservice.repository.TrackArtistRepository;
import org.scuni.artistservice.service.client.TracksClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {
    private final TracksClient tracksClient;

    private final ArtistRepository artistRepository;
    private final TrackArtistRepository trackArtistRepository;

    private final ArtistCreateDtoMapper artistCreateDtoMapper;
    private final ArtistReadDtoMapper artistReadDtoMapper;
    private final ArtistSearchReadDtoMapper artistSearchReadDtoMapper;

    public ArtistReadDto createArtist(ArtistCreateEditDto artistCreateEditDto) {
        Artist artist = artistCreateDtoMapper.map(artistCreateEditDto);
        artist = artistRepository.saveAndFlush(artist);
        log.info("Artist after creating, object: {}", artist);
        return artistReadDtoMapper.map(artist);
    }

    public void updateImageFilenameByImageFilename(String filename, Integer id) {
        artistRepository.updateImageFilenameById(filename, id);
    }

    @Transactional(readOnly = true)
    public ArtistReadDto getArtistById(Integer id, Pageable pageable) {
        return artistRepository.findById(id)
                .map(artist -> {
                    Page<Long> tracksId = artistRepository.findTracksIdByArtistId(id, pageable);
                    Map<String, List<TrackDto>> tracksByTracksId = tracksClient.getTracksByTracksId(tracksId.stream().toList());
                    log.info("tracks: {} fot artist {}", tracksByTracksId, artist);
                    ArtistReadDto artistDto = artistReadDtoMapper.map(artist);
                    artistDto.setTracks(tracksByTracksId.get("tracks"));
                    artistDto.setPageable(tracksId.getPageable());
                    return artistDto;
                })
                .orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
    }

    @Transactional(readOnly = true)
    public Page<ArtistSearchReadDto> getArtistByNickname(String nickname, Pageable pageable) {
        Page<Artist> artists = artistRepository.findByNickname(pageable, nickname);
        return artists.map(artistSearchReadDtoMapper::map);
    }

    public ArtistReadDto updateArtistById(Integer id, ArtistCreateEditDto artistCreateEditDto) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
        artist.setNickname(artistCreateEditDto.getNickname());
        return artistReadDtoMapper.map(artist);
    }

    public void deleteArtistById(Integer id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Failed to delete artist with this id"));
        artistRepository.delete(artist);
    }

    public void addTrackToArtist(Integer artistId, Long trackId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
        if (trackArtistRepository.existsByArtistAndTrackId(artist, trackId))
            throw new TrackAlreadyExistException("The artist already has this track");
        try {
            tracksClient.getTrackByTrackId(trackId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to get track");
        }
        trackArtistRepository.save(
                TrackArtist.builder().
                        artist(artist)
                        .trackId(trackId)
                        .build()
        );
    }
}
