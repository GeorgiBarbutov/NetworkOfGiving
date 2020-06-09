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
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CharityController {
    private final CharityService charityService;

    private final UserService userService;

    private final DonationService donationService;

    private final VolunteerService volunteerService;

    public CharityController(CharityService charityService, UserService userService, DonationService donationService,
                             VolunteerService volunteerService) {
        this.charityService = charityService;
        this.userService = userService;
        this.donationService = donationService;
        this.volunteerService = volunteerService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<CharityResponseDto> getAll() {
        return this.charityService.getAllCharitiesAsResponseDtos();
    }

    @GetMapping(value = "/charity/{id}", produces = "application/json")
    public ResponseEntity<CharityResponseDto> getById(@PathVariable String id) {
        try {
            CharityResponseDto charity = this.charityService.getCharityByIdAsResponseDto(Long.parseLong(id));

            return new ResponseEntity<>(charity, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "charity/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Charity> createCharity(@RequestBody CharityRequestDto charityRequestDto, Authentication authentication){
        try {
            User user = this.userService.findByUsername(authentication.getName());

            this.charityService.addCharity(charityRequestDto, user);

            return new ResponseEntity<>(HttpStatus.OK); //maybe redirect
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
