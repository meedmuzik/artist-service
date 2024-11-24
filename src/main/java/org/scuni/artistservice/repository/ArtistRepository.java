package org.scuni.artistservice.repository;

import org.scuni.artistservice.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistRepository extends Neo4jRepository<Artist, Long> {

    @Query(value = "MATCH (a:Artist) WHERE a.nickname CONTAINS $nickname RETURN a SKIP $skip LIMIT $limit",
            countQuery = "MATCH (a:Artist) WHERE a.nickname CONTAINS $nickname RETURN count(a)")
    Page<Artist> findByNickname(@Param("nickname") String nickname, Pageable pageable);


    @Query("MATCH (a:Artist) WHERE id(a) = $id SET a.imageFilename = $filename")
    void updateImageFilenameById(@Param("filename") String filename, @Param("id") Long id);

    @Query("MATCH (a:Artist), (t:Track) " +
           "WHERE id(a) = $artistId AND id(t) = $trackId " +
           "CREATE (a)-[:HAS_TRACK]->(t)")
    void linkArtistToTrack(@Param("artistId") Long artistId, @Param("trackId") Long trackId);

    @Query("MATCH (c:Comment)-[:COMMENTED_ON]->(t:Track)<-[:HAS_TRACK]-(a:Artist) " +
           "WHERE id(c) IN $ids " +
           "RETURN DISTINCT a")
    List<Artist> finArtistsByCommentsIds(@Param("ids") List<Long> ids);

    @Query(value = """
        MATCH (a:Artist)-[:HAS_TRACK]->(t:Track)<-[:COMMENTED_ON]-(c:Comment)
        WITH a, AVG(c.rating) AS avgTrackRating
        WHERE avgTrackRating > $minRating
        RETURN a
        SKIP $skip LIMIT $limit
        """,
            countQuery = """
        MATCH (a:Artist)-[:HAS_TRACK]->(t:Track)<-[:COMMENTED_ON]-(c:Comment)
        WITH a, AVG(c.rating) AS avgTrackRating
        WHERE avgTrackRating > $minRating
        RETURN count(a)
        """)
    Page<Artist> findArtistsByTrackAverageRatingGreaterThan(@Param("minRating") double minRating, Pageable pageable);


}
