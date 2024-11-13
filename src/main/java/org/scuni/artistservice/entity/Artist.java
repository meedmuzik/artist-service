package org.scuni.artistservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artist {
    @Id
    @GeneratedValue
    private Long id;

    private String nickname;

    private String imageFilename;

    private List<Long> trackIds = new ArrayList<>();

    public boolean idTrackAlreadyExist(Long id){
        return trackIds.contains(id);
    }

    public void addTrack(Long id) {
        this.trackIds.add(id);
    }
}
