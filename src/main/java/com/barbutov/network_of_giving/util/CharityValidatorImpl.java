package com.barbutov.network_of_giving.util;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.util.contracts.CharityValidator;
import org.springframework.stereotype.Component;

@Component
public class CharityValidatorImpl implements CharityValidator {
    public static final String NAME_TOO_LONG = "Charity name is too long";
    public static final String DESCRIPTION_TOO_LONG = "Description is too long";

    @Override
    public String validateCharityDto(CharityRequestDto charityRequestDto) {
        if(charityRequestDto.getName() == null ||
                charityRequestDto.getDescription() == null){
            throw new IllegalArgumentException(Constants.NULL_FIELDS);
        }

        if(charityRequestDto.getName().length() > Constants.CHARITY_NAME_MAX_LENGTH){
            throw new IllegalArgumentException(NAME_TOO_LONG);
        }

        if(charityRequestDto.getDescription().length() > Constants.CHARITY_MAX_DESCRIPTION_LENGTH){
            throw new IllegalArgumentException(DESCRIPTION_TOO_LONG);
        }

        return Constants.VALID;
    }
}
