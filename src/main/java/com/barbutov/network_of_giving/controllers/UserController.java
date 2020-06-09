package com.barbutov.network_of_giving.controllers;

import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.dtos.ProfileDto;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.services.contracts.UserService;
import com.barbutov.network_of_giving.ui.RequestHandler;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    private final UserService userService;
    private final CharityService charityService;
    private final RequestHandler requestHandler;

    public UserController(UserService userService, CharityService charityService, RequestHandler requestHandler) {
        this.userService = userService;
        this.charityService = charityService;
        this.requestHandler = requestHandler;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registrationPost(@RequestBody RegisterDto registerDto) {
        try {
            this.userService.addUser(registerDto);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/register", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> registrationGet(Authentication authentication) {
        try {
            String html = this.requestHandler.handleRequest(authentication, "register", "register");

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/profile/{username}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<ProfileDto> getProfile (@PathVariable String username){
        try {
            ProfileDto profileDto = getProfileDto(username);

            return new ResponseEntity<>(profileDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/profile", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<ProfileDto> getMyProfile (Authentication authentication){
        try {
            String username = authentication.getName();
            ProfileDto profileDto = getProfileDto(username);

            return new ResponseEntity<>(profileDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private ProfileDto getProfileDto(String username) throws NotFoundException {
        User user = this.userService.findByUsername(username);

        List<CharityResponseDto> donatedToCharities = this.charityService.findAllDonatedToCharities(username);
        List<CharityResponseDto> volunteeredToCharities = this.charityService.findAllVolunteeredToCharities(username);
        List<CharityResponseDto> createdCharities = this.charityService.findAllCreatedCharities(username);

        return new ProfileDto(user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getGender(), user.getAge(), createdCharities, donatedToCharities, volunteeredToCharities);
    }
}
