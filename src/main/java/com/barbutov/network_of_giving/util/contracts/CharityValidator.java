package com.barbutov.network_of_giving.util.contracts;

import com.barbutov.network_of_giving.data.dtos.CharityRequestDto;

public interface CharityValidator {
    String validateCharityDto(CharityRequestDto charityRequestDto);
}
