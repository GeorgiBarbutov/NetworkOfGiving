package com.barbutov.network_of_giving.controllers;

import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.dtos.ProfileDto;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.services.contracts.UserService;
import com.barbutov.network_of_giving.ui.RequestHandler;
import com.barbutov.network_of_giving.util.Constants;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<String> registrationPost(@RequestBody RegisterDto registerDto, Authentication authentication) {
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            //you can't register if you are logged in
            if(authentication != null){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            this.userService.addUser(registerDto);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/register", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> registrationGet(Authentication authentication) {
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);
            //you can't register if you are logged in
            if(authentication != null){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            String html = this.requestHandler.handleRequest(null, Constants.REGISTER_FILE_NAME,
                    Constants.REGISTER_FILE_NAME);

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getLogin (Authentication authentication){
        String errorHtml = "";

        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);
            //you can't login if you are logged in
            if(authentication != null){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            String html = this.requestHandler.handleRequest(null, Constants.LOGIN_FILE_NAME);

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/profile/{username}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getProfile (@PathVariable String username, Authentication authentication){
        String errorHtml = "";

        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);
            ProfileDto profileDto = getProfileDto(username);

            Object[][] models = getProfileModels(profileDto);

            String html = this.requestHandler.handleRequest(authentication, Constants.PROFILE_FILE_NAME,
                    Constants.PROFILE_FILE_NAME, models, new String[] { Constants.CHARITY_DETAILS_TEMPLATE_NAME,
                             Constants.CHARITY_DETAILS_TEMPLATE_NAME, Constants.CHARITY_DETAILS_TEMPLATE_NAME });

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/profile", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getMyProfile (Authentication authentication){
        return getProfile(authentication.getName(), authentication);
    }

    @GetMapping(value = "/user", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getCurrentUsername (Authentication authentication){
        return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
    }

    private ProfileDto getProfileDto(String username) throws NotFoundException {
        User user = this.userService.findByUsername(username);

        List<CharityResponseDto> donatedToCharities = this.charityService.findAllDonatedToCharities(username);
        List<CharityResponseDto> volunteeredToCharities = this.charityService.findAllVolunteeredToCharities(username);
        List<CharityResponseDto> createdCharities = this.charityService.findAllCreatedCharities(username);

        return new ProfileDto(user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getGender(), user.getAge(), createdCharities, donatedToCharities, volunteeredToCharities);
    }

    private Object[][] getProfileModels(ProfileDto profileDto){
        List<CharityResponseDto> createdCharities = profileDto.getCreatedCharities();
        List<CharityResponseDto> volunteeredInCharities = profileDto.getVolunteeredInCharities();
        List<CharityResponseDto> donatedToCharities = profileDto.getDonatedToCharities();

        Object[][] models = new Object[4][];

        models[0] = new Object[createdCharities.size()];
        for (int i = 0; i < createdCharities.size(); i++) {
            models[0][i] = createdCharities.get(i);
        }

        models[1] = new Object[volunteeredInCharities.size()];
        for (int i = 0; i < volunteeredInCharities.size(); i++) {
            models[1][i] = volunteeredInCharities.get(i);
        }

        models[2] = new Object[donatedToCharities.size()];
        for (int i = 0; i < donatedToCharities.size(); i++) {
            models[2][i] = donatedToCharities.get(i);
        }

        models[3] = new Object[1];
        models[3][0] = profileDto;

        return models;
    }
}
