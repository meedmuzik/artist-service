package org.scuni.artistservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.scuni.artistservice.dto.ArtistCreateEditDto;
import org.scuni.artistservice.dto.ArtistReadDto;
import org.scuni.artistservice.dto.ArtistSearchReadDto;
import org.scuni.artistservice.dto.QueryDto;
import org.scuni.artistservice.dto.TrackDto;
import org.scuni.artistservice.entity.Artist;
import org.scuni.artistservice.exception.ArtistNotFoundException;
import org.scuni.artistservice.exception.TrackAlreadyExistException;
import org.scuni.artistservice.mapper.ArtistCreateDtoMapper;
import org.scuni.artistservice.mapper.ArtistReadDtoMapper;
import org.scuni.artistservice.mapper.ArtistSearchReadDtoMapper;
import org.scuni.artistservice.repository.ArtistRepository;
import org.scuni.artistservice.service.client.CommentsClient;
import org.scuni.artistservice.service.client.TracksClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    private final ArtistReadDtoMapper artistReadDtoMapper;
    private final ArtistCreateDtoMapper artistCreateDtoMapper;
    private final ArtistSearchReadDtoMapper artistSearchReadDtoMapper;
    private final TracksClient tracksClient;
    private final CommentsClient commentsClient;

    public ArtistReadDto createArtist(ArtistCreateEditDto artistCreateEditDto) {
        Artist artist = artistCreateDtoMapper.map(artistCreateEditDto);
        artist = artistRepository.save(artist);
        return artistReadDtoMapper.map(artist);
    }

    public void updateImageFilenameByImageFilename(String filename, Long id) {
        artistRepository.updateImageFilenameById(filename, id);
    }

    public ArtistReadDto getArtistById(Long id, Pageable pageable) {
        return artistRepository.findById(id)
                .map(artistReadDtoMapper::map)
                .map(artistReadDto -> {
                    log.info("Переданные ids: {}", artistReadDto.getTrackIds());
                    Map<String, List<TrackDto>> tracksByTracksId = tracksClient.getTracksByTracksId(artistReadDto.getTrackIds());

                    log.info("Список треков: {}", tracksByTracksId.get("tracks"));
                    artistReadDto.setTracks(tracksByTracksId.get("tracks"));
                    artistReadDto.setPageable(pageable);
                    return artistReadDto;
                })
                .orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
    }

    public Page<ArtistSearchReadDto> getArtistByNickname(String nickname, Pageable pageable) {
        Page<Artist> artists = artistRepository.findByNickname(nickname, pageable);
        return artists.map(artistSearchReadDtoMapper::map);
    }

    public ArtistReadDto updateArtistById(Long id, ArtistCreateEditDto artistCreateEditDto) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
        artist.setNickname(artistCreateEditDto.getNickname());
        artistRepository.save(artist);
        return artistReadDtoMapper.map(artist);
    }

    public void deleteArtistById(Long id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Failed to delete artist with this id"));
        artistRepository.delete(artist);
    }

    public void addTrackToArtist(Long artistId, Long trackId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Failed to get artist"));
        if (artist.idTrackAlreadyExist(trackId))
            throw new TrackAlreadyExistException("The artist already has this track");
        try {
            tracksClient.getTrackByTrackId(trackId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to get track");
        }
        artist.addTrack(trackId);
        artistRepository.save(artist);
        artistRepository.linkArtistToTrack(artistId, trackId);
    }

    public List<ArtistReadDto> getRecommendedArtists(QueryDto query) {
        log.info("query {}", query);
        Map<String, List<Long>> recommendedCommentsIds = commentsClient.getRecommendedCommentsIds(query);
        log.info("comments ids: {}", recommendedCommentsIds.get("commentsIds"));
        return artistRepository.finArtistsByCommentsIds(recommendedCommentsIds.get("commentsIds"))
                .stream()
                .distinct()
                .map(artistReadDtoMapper::map)
                .collect(Collectors.toList());
    }
    public Page<ArtistSearchReadDto> getArtistsByAverageTrackRatingGreaterThan(double minRating, Pageable pageable) {
        Page<Artist> artists = artistRepository.findArtistsByTrackAverageRatingGreaterThan(minRating, pageable);
        return artists.map(artistSearchReadDtoMapper::map);
    }

    public List<ArtistReadDto> getFeaturedArtistByArtistId(long artistId) {
        Set<Artist> featuredArtists = artistRepository.findFeaturedArtists(artistId);
        Set<Artist> result = new HashSet<>(featuredArtists);
        for (Artist featuredArtist : featuredArtists) {
            Set<Artist> foundArtists = artistRepository.findFeaturedArtists(featuredArtist.getId());
            foundArtists.stream()
                    .filter(artist -> artist.getId() != artistId)
                    .forEach(result::add);
        }
        return result.stream().map(artistReadDtoMapper::map).toList();
    }

    public void updateArtistRating(Long id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Double updatedRating = artistRepository.calculateArtistRating(id);
        if (updatedRating == null) {
            updatedRating = 0.0;
        }
        artist.setRating(updatedRating);
        artistRepository.save(artist);
    }
}
