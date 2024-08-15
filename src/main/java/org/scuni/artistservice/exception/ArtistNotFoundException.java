package org.scuni.artistservice.exception;

public class ArtistNotFoundException extends RuntimeException{
    public ArtistNotFoundException(String message) {
        super(message);
    }
}
