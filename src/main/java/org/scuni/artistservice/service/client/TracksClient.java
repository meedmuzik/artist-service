package org.scuni.artistservice.service.client;

import org.scuni.artistservice.dto.TrackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient("tracks-service")
public interface TracksClient {
    @GetMapping(value = "api/v1/tracks", consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<TrackDto>> getTracksByTracksId(@RequestParam List<Long> ids);

    @GetMapping(value = "api/v1/track/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    TrackDto getTrackByTrackId(@PathVariable Long id);
}
