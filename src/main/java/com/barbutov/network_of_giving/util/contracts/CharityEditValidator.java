package com.barbutov.network_of_giving.util.contracts;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;

public interface CharityEditValidator {
    String validateCharityDto(CharityRequestDto charityRequestDto, double collectedAmount, double volunteersCount);
}
