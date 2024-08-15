package org.scuni.artistservice.service.client;

import org.scuni.artistservice.dto.TrackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient("tracks-service")
public interface TracksClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "api/v1/tracks/}",
            consumes = "application/json"
    )
    List<TrackDto> getTracksByTracksId(@RequestBody List<Integer> tracksId);
}
