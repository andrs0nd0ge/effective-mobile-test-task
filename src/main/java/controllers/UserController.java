package controllers;

import dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import models.AuthenticationRequest;
import models.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import services.CustomUserDetailsService;
import util.JwtUtil;

@RestController
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Generates a JWT based on provided credentials")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "JWT was generated successfully",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "This status code cannot be returned for this endpoint",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error: something wrong on the server side",
                    content = {@Content
                            (mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                    }
            )
    })
    @PostMapping("generate_jwt")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
