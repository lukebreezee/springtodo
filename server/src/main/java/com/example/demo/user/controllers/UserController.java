package com.example.demo.user.controllers;

import com.example.demo.payload.request.auth.RegisterRequest;
import com.example.demo.payload.request.auth.SignInRequest;
import com.example.demo.payload.request.auth.NewAccessTokenRequest;
import com.example.demo.payload.response.GenericResponse;
import com.example.demo.payload.response.auth.SigninResponseJwt;
import com.example.demo.payload.response.auth.TokenResponse;
import com.example.demo.refreshToken.RefreshToken;
import com.example.demo.refreshToken.RefreshTokenService;
import com.example.demo.todo.Todo;
import com.example.demo.todo.TodoService;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import com.example.demo.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("user")
    public String getUser(@RequestHeader Map<String, String> headers) {
        Optional<User> foundUser = userService.getUserById(Long.parseLong(headers.get("id")));
        return foundUser.get().getName();
    }

    @PostMapping(path = "user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> register(@RequestBody RegisterRequest newUser) {
        try {
            String passhash = this.passwordEncoder.encode(newUser.getPassword());

            User userToPost = new User(
                    newUser.getName(),
                    newUser.getEmail(),
                    passhash
            );
            userService.saveUser(userToPost);

            GenericResponse successResponse = new GenericResponse(true, "User saved to database");

            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            String exceptionMessage = e.getMessage();
            String err;
            if (exceptionMessage.contains("constraint [users_email_key]")) {
                err = "Email is already in use";
            } else {
                err = "An error has occurred";
            }

            GenericResponse failureResponse = new GenericResponse(false, err);

            return new ResponseEntity<>(failureResponse, HttpStatus.CREATED);
        }
    }

    @PostMapping(path = "sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigninResponseJwt> signIn(@RequestBody SignInRequest signInDetails) {
        SigninResponseJwt signInResponse;
        try {
            List<User> queryResponse = userService.getUserByEmail(signInDetails.getEmail());
            if (queryResponse.isEmpty()) {
                signInResponse = new SigninResponseJwt(false, "Username or password is incorrect", "", "");
            } else {
                User foundUser = queryResponse.get(0);
                String submittedPassword = signInDetails.getPassword();
                String foundPasshash = foundUser.getPasshash();
                if (!passwordEncoder.matches(submittedPassword, foundPasshash)) {
                    signInResponse = new SigninResponseJwt(false, "Username or password is incorrect", "", "");
                    return new ResponseEntity<>(signInResponse, HttpStatus.ACCEPTED);
                }
                String userId = foundUser.getId().toString();
                String accessToken = jwtUtil.generateToken(userId);

                RefreshToken refreshToken = new RefreshToken(jwtUtil.generateRefreshToken(userId));
                refreshTokenService.saveRefreshToken(refreshToken);

                signInResponse = new SigninResponseJwt(true, "User found", accessToken, refreshToken.getToken());
            }

            return new ResponseEntity<>(signInResponse, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            signInResponse = new SigninResponseJwt(false, "An error has occurred", "", "");
            return new ResponseEntity<>(signInResponse, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("refresh_token")
    public List<RefreshToken> getRefreshTokens() {
        return refreshTokenService.getTokens();
    }

    @PostMapping("token")
    public ResponseEntity<TokenResponse> getNewAccessToken(@RequestBody NewAccessTokenRequest request) {
        TokenResponse response;

        String token = request.getToken();

        List<RefreshToken> tokenQuery = refreshTokenService.getToken(token);

        if (tokenQuery.isEmpty()) {
            response = new TokenResponse(false, "Refresh token not found", "");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        String id = jwtUtil.extractRefreshId(token);

        String newToken = jwtUtil.generateToken(id);
        response = new TokenResponse(true, "New access token created", newToken);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("token")
    @Transactional
    public ResponseEntity<GenericResponse> deleteRefreshToken(@RequestHeader Map<String, String> headers) {
        try {
            String token = headers.get("token");
            System.out.println(token);
            refreshTokenService.deleteRefreshToken(token);
            return new ResponseEntity<>(
                    new GenericResponse(true, "Token deleted"),
                    HttpStatus.ACCEPTED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new GenericResponse(false, "An error has occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

}
