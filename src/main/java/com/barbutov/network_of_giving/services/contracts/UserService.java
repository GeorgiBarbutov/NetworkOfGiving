package com.barbutov.network_of_giving.services.contracts;

import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import javassist.NotFoundException;

public interface UserService{
    void addUser(RegisterDto registerDto);
    User findByUsername(String username) throws NotFoundException;
}
