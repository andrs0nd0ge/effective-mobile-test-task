package models;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    private final String jwt;
}
