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
import com.barbutov.network_of_giving.util.Constants;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.scanner.Constant;

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
        String errorHtml = "";

        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            List<CharityResponseDto> responseDtos = this.charityService.getAllCharitiesAsResponseDtos();

            Object[][] models = new Object[1][];
            models[0] = new Object[responseDtos.size()];
            for (int i = 0; i < responseDtos.size(); i++) {
                models[0][i] = responseDtos.get(i);
            }

            String html = this.requestHandler.handleRequest(authentication, Constants.CHARITIES_FILE_NAME,
                    Constants.CHARITIES_FILE_NAME, models, new String[] { Constants.CHARITY_DETAILS_TEMPLATE_NAME });

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/charity/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getById(Authentication authentication, @PathVariable String id) {
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            CharityResponseDto responseDto = this.charityService.getCharityByIdAsResponseDto(Long.parseLong(id));
            Object[] model = new Object[1];
            model[0] = responseDto;

            String html = this.requestHandler.handleRequest(authentication, Constants.CHARITY_FILE_NAME,
                    Constants.CHARITY_FILE_NAME, model);

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/charity/name/{name}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getByName(Authentication authentication, @PathVariable String name) {
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            CharityResponseDto responseDto = this.charityService.getCharityByName(name);
            Object[] model = new Object[1];
            model[0] = responseDto;

            String html = this.requestHandler.handleRequest(authentication, Constants.CHARITY_FILE_NAME,
                    Constants.CHARITY_FILE_NAME, model);

            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "charity/create", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity getCreateCharity(Authentication authentication){
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            String html = this.requestHandler.handleRequest(authentication, Constants.CREATE_CHARITY_TEMPLATE_NAME,
                    Constants.CREATE_CHARITY_TEMPLATE_NAME);
            return new ResponseEntity<>(html, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "charity/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCharity(@RequestBody CharityRequestDto charityRequestDto,
                                              Authentication authentication){
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            User user = this.userService.findByUsername(authentication.getName());

            Charity charity = this.charityService.addCharity(charityRequestDto, user);

            return new ResponseEntity<>(String.valueOf(charity.getId()), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "charity/delete/{id}")
    public ResponseEntity<String> deleteCharity(@PathVariable String id, Authentication authentication){
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            String username = authentication.getName();

            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityByIdAndCreator(Long.parseLong(id), user);

            this.volunteerService.removeAllVolunteersByCharity(charity);
            this.donationService.removeAllDonationsByCharity(charity);

            this.charityService.deleteCharity(Long.parseLong(id));

            return ResponseEntity.ok().location(URI.create("/")).build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "charity/donate/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> donateToCharity(@RequestBody AmountDto amountDto, @PathVariable String id,
                                                   Authentication authentication){
        String errorHtml = "";
        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            double amount = amountDto.getAmount();

            //Can't be negative
            if(amount < 0){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            //Going over budget
            if(charity.getCollectedAmount() + amount > charity.getBudgetRequired()){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            this.charityService.donateToCharity(charity, amount);
            this.donationService.addDonation(charity, amount, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "charity/volunteer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> volunteerInCharity(@PathVariable String id, Authentication authentication){
        String errorHtml = "";

        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            //You can't volunteer twice
            if(this.volunteerService.existsByCharityAndUser(charity, user)){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            this.charityService.volunteerToCharity(charity);
            this.volunteerService.addVolunteer(charity, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "charity/withhold/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> withholdFromCharity(@PathVariable String id, Authentication authentication){
        String errorHtml = "";

        try {
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);

            String username = authentication.getName();
            User user = this.userService.findByUsername(username);
            Charity charity = this.charityService.getCharityById(Long.parseLong(id));

            //You haven't volunteered for this charity
            if(!this.volunteerService.existsByCharityAndUser(charity, user)){
                return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
            }

            this.charityService.withholdFromCharity(charity);
            this.volunteerService.removeVolunteer(charity, user);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "isVolunteered/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getIsVolunteered(@PathVariable String id, Authentication authentication){
        String errorHtml = "";
        try{
            errorHtml = this.requestHandler.handleRequest(authentication, Constants.ERROR_TEMPLATE_NAME);
            String username = authentication.getName();

            boolean isVolunteered = this.volunteerService.existsByCharityIdAndUsername(Long.parseLong(id), username);

            return new ResponseEntity<>(String.valueOf(isVolunteered), HttpStatus.OK);
        } catch (NumberFormatException e){
            return new ResponseEntity<>(errorHtml, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(errorHtml, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
