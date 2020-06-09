package com.barbutov.network_of_giving.util.contracts;

import com.barbutov.network_of_giving.data.dtos.RegisterDto;

public interface RegisterValidator {
    String validateRegisterDto(RegisterDto registerDto);
}
