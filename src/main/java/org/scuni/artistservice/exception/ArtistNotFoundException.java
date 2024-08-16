package org.scuni.artistservice.exception;

public class ArtistNotFoundException extends DefaultArtistServiceException {
    public ArtistNotFoundException(String message) {
        super(message);
    }
}
