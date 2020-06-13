package com.barbutov.network_of_giving.util;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;
import com.barbutov.network_of_giving.util.contracts.CharityEditValidator;
import com.barbutov.network_of_giving.util.contracts.CharityValidator;
import org.springframework.stereotype.Component;

@Component
public class CharityEditValidatorImpl implements CharityEditValidator {
    private static final String BUDGET_TOO_LOW = "Budget can't be less then the already collected amount";
    private static final String DESIRED_PARTICIPANTS_TOO_LOW = "Desired participants can't be less then the already volunteered count";

    private final CharityValidator charityValidator;

    public CharityEditValidatorImpl(CharityValidator charityValidator) {
        this.charityValidator = charityValidator;
    }

    @Override
    public String validateCharityDto(CharityRequestDto charityRequestDto, double collectedAmount, double volunteersCount) {
        if(charityRequestDto.getBudgetRequired() < collectedAmount){
            throw new IllegalArgumentException(BUDGET_TOO_LOW);
        }

        if(charityRequestDto.getDesiredParticipants() < volunteersCount){
            throw new IllegalArgumentException(DESIRED_PARTICIPANTS_TOO_LOW);
        }

        this.charityValidator.validateCharityDto(charityRequestDto);

        return Constants.VALID;
    }
}
