package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.contracts.UserRepository;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.UserService;
import com.barbutov.network_of_giving.util.contracts.RegisterValidator;
import com.barbutov.network_of_giving.util.RegisterValidatorImpl;
import javassist.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERNAME_NOT_FOUND = "Wrong username";
    private static final String USER_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RegisterValidator registerValidator;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, RegisterValidator registerValidatorImpl) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.registerValidator = registerValidatorImpl;
    }

    @Override
    public void addUser(RegisterDto registerDto) {
        String validation = this.registerValidator.validateRegisterDto(registerDto);

        if(!validation.equals(RegisterValidatorImpl.VALID)){
            throw new IllegalArgumentException(validation);
        }

        String encodedPassword = encoder.encode(registerDto.getPassword());

        User user = new User(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getUsername(),
                encodedPassword, registerDto.getGender(), registerDto.getAge(), registerDto.getLocation(), USER_ROLE);

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) throws NotFoundException {
        Optional<User> optional = this.userRepository.findByUsername(username);

        if(optional.isPresent()){
            return optional.get();
        } else {
            throw new NotFoundException(USERNAME_NOT_FOUND);
        }
    }
}
