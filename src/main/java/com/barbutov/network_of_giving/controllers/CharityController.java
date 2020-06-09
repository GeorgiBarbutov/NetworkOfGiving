package com.barbutov.network_of_giving.controllers;

import com.barbutov.network_of_giving.data.dtos.AmountDto;
import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.services.contracts.DonationService;
import com.barbutov.network_of_giving.services.contracts.UserService;
import com.barbutov.network_of_giving.services.contracts.VolunteerService;
import com.barbutov.network_of_giving.ui.RequestHandler;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class CharityController {
    private final CharityService charityService;
    private final UserService userService;
    private final DonationService donationService;
    private final VolunteerService volunteerService;
    private final RequestHandler requestHandler;

    public CharityController(CharityService charityService, UserService userService, DonationService donationService,
                             VolunteerService volunteerService, RequestHandler requestHandler) {
        this.charityService = charityService;
        this.userService = userService;
        this.donationService = donationService;
        this.volunteerService = volunteerService;
        this.requestHandler = requestHandler;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getAll(Authentication authentication) {
        try {
            List<CharityResponseDto> responseDtos = this.charityService.getAllCharitiesAsResponseDtos();

            Object[] model = new Object[responseDtos.size()];
            for (int i = 0; i < responseDtos.size(); i++) {
                model[i] = responseDtos.get(i);
            }

            String html = this.requestHandler.handleRequest(authentication, "charities", "charities",
                    model, "charityDetails");

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/charity/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getById(Authentication authentication, @PathVariable String id) {
        try {
            CharityResponseDto responseDto = this.charityService.getCharityByIdAsResponseDto(Long.parseLong(id));
            Object[] model = new Object[1];
            model[0] = responseDto;

            String html = this.requestHandler.handleRequest(authentication, "charity", "charity", model);

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "charity/create", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity getCreateCharity(Authentication authentication){
        try {
            String html = this.requestHandler.handleRequest(authentication, "createCharity", "createCharity");
            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "charity/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createCharity(@RequestBody CharityRequestDto charityRequestDto, Authentication authentication){
        try {
            User user = this.userService.findByUsername(authentication.getName());

            Charity charity = this.charityService.addCharity(charityRequestDto, user);

            return new ResponseEntity<>(charity.getId(), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "charity/delete/{id}")
    public ResponseEntity<Charity> deleteCharity(@PathVariable String id, Authentication authentication){
        try {
            String username = authentication.getName();

            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityByIdAndCreator(Long.parseLong(id), user);

            this.volunteerService.removeAllVolunteersByCharity(charity);
            this.donationService.removeAllDonationsByCharity(charity);

            this.charityService.deleteCharity(Long.parseLong(id));

            return ResponseEntity.ok().location(URI.create("/")).build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "charity/donate/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charity> donateToCharity(@RequestBody AmountDto amountDto, @PathVariable String id,
                                                   Authentication authentication){
        try {
            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            double amount = amountDto.getAmount();

            //Can't be negative
            if(amount < 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //Going over budget
            if(charity.getCollectedAmount() + amount > charity.getBudgetRequired()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            this.charityService.donateToCharity(charity, amount);
            this.donationService.addDonation(charity, amount, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "charity/volunteer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charity> volunteerInCharity(@PathVariable String id, Authentication authentication){
        try {
            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            //You can't volunteer twice
            if(this.volunteerService.existsByCharityAndUser(charity, user)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            this.charityService.volunteerToCharity(charity);
            this.volunteerService.addVolunteer(charity, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "charity/withhold/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charity> withholdFromCharity(@PathVariable String id, Authentication authentication){
        try {
            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            //You haven't volunteered for this charity
            if(!this.volunteerService.existsByCharityAndUser(charity, user)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            this.charityService.withholdFromCharity(charity);
            this.volunteerService.removeVolunteer(charity, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
