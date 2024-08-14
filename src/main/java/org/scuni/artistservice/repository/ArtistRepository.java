package org.scuni.artistservice.repository;

import lombok.RequiredArgsConstructor;
import org.scuni.artistservice.dto.ArtistCreateDto;
import org.scuni.artistservice.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Artist a SET a.imageFilename = :imageFilename WHERE a.id = :id")
    void updateImageFilenameById(@Param("imageFilename") String imageFilename, @Param("id") Integer id);

}
