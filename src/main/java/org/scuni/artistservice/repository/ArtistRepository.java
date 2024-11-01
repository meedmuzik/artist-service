package org.scuni.artistservice.repository;

import org.scuni.artistservice.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends Neo4jRepository<Artist, Long> {
    @Query(value = "MATCH (a:Artist) WHERE a.nickname CONTAINS $nickname RETURN a SKIP $skip LIMIT $limit",
            countQuery = "MATCH (a:Artist) WHERE a.nickname CONTAINS $nickname RETURN count(a)")
    Page<Artist> findByNickname(@Param("nickname") String nickname, Pageable pageable);


    @Query("MATCH (a:Artist) WHERE id(a) = $id SET a.imageFilename = $filename")
    void updateImageFilenameById(@Param("filename") String filename, @Param("id") Long id);

}
