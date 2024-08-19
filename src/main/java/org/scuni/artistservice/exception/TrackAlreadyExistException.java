package org.scuni.artistservice.exception;

public class TrackAlreadyExistException extends DefaultArtistServiceException {
    public TrackAlreadyExistException(String message) {
        super(message);
    }
}
