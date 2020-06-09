package com.barbutov.network_of_giving.controllers;

import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.dtos.ProfileDto;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.services.contracts.UserService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    private final UserService userService;

    private final CharityService charityService;

    public UserController(UserService userService, CharityService charityService) {
        this.userService = userService;
        this.charityService = charityService;
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

    @GetMapping(value = "/profile/{username}", produces = "application/json")
    public ResponseEntity<ProfileDto> getProfile (@PathVariable String username){
        try {
            User user = this.userService.findByUsername(username);

            List<CharityResponseDto> donatedToCharities = this.charityService.findAllDonatedToCharities(username);
            List<CharityResponseDto> volunteeredToCharities = this.charityService.findAllVolunteeredToCharities(username);
            List<CharityResponseDto> createdCharities = this.charityService.findAllCreatedCharities(username);

            ProfileDto profileDto = new ProfileDto(user.getFirstName(), user.getLastName(), user.getUsername(),
                    user.getGender(), user.getAge(), createdCharities, donatedToCharities, volunteeredToCharities);

            return new ResponseEntity<>(profileDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
