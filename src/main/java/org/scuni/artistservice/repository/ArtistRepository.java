package org.scuni.artistservice.repository;

import org.scuni.artistservice.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Modifying
    @Query("update Artist a set a.imageFilename = :imageFilename where a.id = :id")
    void updateImageFilenameById(@Param("imageFilename") String imageFilename, @Param("id") Integer id);


    @Query("select ta.trackId from TrackArtist ta where ta.artist.id = :artistId")
    List<Integer> findTracksIdByArtistId(Integer artistId);

    @Query("select a from Artist a where a.nickname like %:nickname%")
    Page<Artist> findByNickname(Pageable pageable, String nickname);
}
