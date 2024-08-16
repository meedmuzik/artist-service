package org.scuni.artistservice.repository;

import org.scuni.artistservice.entity.Artist;
import org.scuni.artistservice.entity.TrackArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrackArtistRepository extends JpaRepository<TrackArtist, Integer> {
    boolean existsByArtistAndTrackId(Artist artist, Long trackId);
}
