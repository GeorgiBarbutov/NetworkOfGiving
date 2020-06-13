package com.barbutov.network_of_giving.util;

import com.barbutov.network_of_giving.data.contracts.CharityRepository;
import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.util.contracts.CharityValidator;
import org.springframework.stereotype.Component;

@Component
public class CharityValidatorImpl implements CharityValidator {
    private static final String NAME_TOO_LONG = "Charity name is too long";
    private static final String DESCRIPTION_TOO_LONG = "Description is too long";
    private static final String CHARITY_WITH_NAME_EXISTS = "Charity with this name already exists!";

    private final CharityRepository charityRepository;

    public CharityValidatorImpl(CharityRepository charityRepository) {
        this.charityRepository = charityRepository;
    }

    @Override
    public String validateCharityDto(CharityRequestDto charityRequestDto) {
        if(charityRequestDto.getName().isBlank() ||
                charityRequestDto.getDescription().isBlank()){
            throw new IllegalArgumentException(Constants.NULL_OR_EMPTY_FIELDS);
        }

        if(charityRequestDto.getName().length() > Constants.CHARITY_NAME_MAX_LENGTH){
            throw new IllegalArgumentException(NAME_TOO_LONG);
        }

        if(charityRequestDto.getDescription().length() > Constants.CHARITY_MAX_DESCRIPTION_LENGTH){
            throw new IllegalArgumentException(DESCRIPTION_TOO_LONG);
        }

        if(this.charityRepository.findByName(charityRequestDto.getName()).isPresent()){
            throw new IllegalArgumentException(CHARITY_WITH_NAME_EXISTS);
        }

        return Constants.VALID;
    }
}
