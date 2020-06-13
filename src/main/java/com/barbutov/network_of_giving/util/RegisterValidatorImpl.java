package com.barbutov.network_of_giving.util;

import com.barbutov.network_of_giving.data.contracts.UserRepository;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.util.contracts.RegisterValidator;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidatorImpl implements RegisterValidator {

    public static final String PASSWORD_MISMATCH = "Password Mismatch";
    public static final String AGE_OUTSIDE_OF_RANGE = "Age is out side of range";
    public static final String FIRST_NAME_TOO_LONG = "First Name is too long";
    public static final String LAST_NAME_TOO_LONG = "Last Name is too long";
    public static final String PASSWORD_TOO_LONG = "Password is too long";
    public static final String USERNAME_TOO_LONG = "Username is too long";
    private static final String LOCATION_TOO_LONG = "Location is too long";
    public static final String USER_IS_TAKEN = "Username is taken";

    private UserRepository userRepository;

    public RegisterValidatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validateRegisterDto(RegisterDto registerDto){
        if(registerDto.getPassword().isBlank() || registerDto.getPasswordConfirm().isBlank() ||
                registerDto.getUsername().isBlank() || registerDto.getFirstName().isBlank() ||
                registerDto.getLastName().isBlank() || registerDto.getLocation().isBlank()){
            return Constants.NULL_OR_EMPTY_FIELDS;
        }

        if(!registerDto.getPassword().equals(registerDto.getPasswordConfirm())){
            return PASSWORD_MISMATCH;
        }

        if(registerDto.getAge() < Constants.USER_AGE_MIN_VALUE || registerDto.getAge() >= Constants.USER_AGE_MAX_VALUE){
            return AGE_OUTSIDE_OF_RANGE;
        }

        if(registerDto.getFirstName().length() > Constants.USER_FIRST_NAME_MAX_LENGTH){
            return FIRST_NAME_TOO_LONG;
        }

        if(registerDto.getLastName().length() > Constants.USER_LAST_NAME_MAX_LENGTH){
            return LAST_NAME_TOO_LONG;
        }

        if(registerDto.getUsername().length() > Constants.USER_USERNAME_MAX_LENGTH){
            return USERNAME_TOO_LONG;
        }

        if(registerDto.getPassword().length() > Constants.USER_PASSWORD_MAX_LENGTH){
            return PASSWORD_TOO_LONG;
        }

        if(registerDto.getLocation().length() > Constants.USER_LOCATION_MAX_LENGTH){
            return LOCATION_TOO_LONG;
        }

        if(this.userRepository.findByUsername(registerDto.getUsername()).isPresent()){
            return USER_IS_TAKEN;
        }

        return Constants.VALID;
    }
}
