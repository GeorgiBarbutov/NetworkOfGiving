package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.Gender;
import com.barbutov.network_of_giving.data.contracts.UserRepository;
import com.barbutov.network_of_giving.data.dtos.RegisterDto;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.UserService;
import com.barbutov.network_of_giving.util.Constants;
import com.barbutov.network_of_giving.util.contracts.RegisterValidator;
import com.barbutov.network_of_giving.util.RegisterValidatorImpl;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    private User testUser;
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        RegisterValidator registerValidator = new RegisterValidatorImpl(this.userRepository);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.userService = new UserServiceImpl(this.userRepository, encoder, registerValidator);

        this.testUser = new User("test", "user", "test", "1234", Gender.MALE, 20,
                "Sofia", "USER");
    }

    @Test
    public void userServiceFindByUsernameReturnFoundUser() {
        String username = "test";

        Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        try {
            User user = this.userService.findByUsername(username);
            assertEquals(username, user.getUsername());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userServiceFindByUsernameThrowsNotFoundException() {
        String username = "test";
        boolean thrown = false;
        String exception = "";

        Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));
        try {
            this.userService.findByUsername(username);
        } catch (NotFoundException e) {
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Wrong username", exception);
    }

    @Test
    public void userServiceAddsUsersSuccessfully() {
        RegisterDto correctRegisterDto = new RegisterDto("register", "test", "register",
                "1234", "1234", Gender.MALE, 23, "Sofia");

        Mockito.when(this.userRepository.findByUsername(correctRegisterDto.getUsername())).thenReturn(Optional.ofNullable(null));
        Mockito.when(this.userRepository.save(testUser)).thenReturn(testUser);

        userService.addUser(correctRegisterDto);
    }

    @Test
    public void userServiceThrowsExceptionWhenUsernameIsTaken() {
        RegisterDto takenUsername = new RegisterDto("test", "test", "test",
                "1234", "1234", Gender.MALE, 23, "Sofia");
        boolean thrown = false;
        String exception = "";

        Mockito.when(this.userRepository.findByUsername(takenUsername.getUsername())).thenReturn(Optional.of(testUser));
        Mockito.when(this.userRepository.save(testUser)).thenReturn(testUser);

        try {
            userService.addUser(takenUsername);
        } catch (IllegalArgumentException e) {
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals(RegisterValidatorImpl.USER_IS_TAKEN, exception);
    }

    @Test
    public void userServiceThrowsExceptionOnNullParametersWhenAddingUsersWithIncorrectParameters() {
        RegisterDto nullRegisterDto = new RegisterDto("", "test", "register",
                "1234", "1234", Gender.MALE, 23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(nullRegisterDto, Constants.NULL_OR_EMPTY_FIELDS);
    }

    @Test
    public void userServiceThrowsExceptionPasswordMismatchWhenAddingUsersWithIncorrectParameters() {
        RegisterDto passwordMismatch = new RegisterDto("register", "test", "register",
                "1234", "1234567", Gender.MALE, 23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(passwordMismatch, RegisterValidatorImpl.PASSWORD_MISMATCH);
    }

    @Test
    public void userServiceThrowsExceptionAgeOutSideOfRangeWhenAddingUsersWithIncorrectParameters() {
        RegisterDto ageTooBig = new RegisterDto("register", "test", "register", "1234",
                "1234", Gender.MALE, 23213, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(ageTooBig, RegisterValidatorImpl.AGE_OUTSIDE_OF_RANGE);
    }

    @Test
    public void userServiceThrowsExceptionWhenFirstNameIsTooLongWhenAddingUsers() {
        RegisterDto firstNameTooLong = new RegisterDto("registerregisterregisterregisterregisterregisterregisterregister",
                "test", "register", "1234", "1234", Gender.MALE, 23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(firstNameTooLong, RegisterValidatorImpl.FIRST_NAME_TOO_LONG);
    }

    @Test
    public void userServiceThrowsExceptionWhenLastNameIsTooLongWhenAddingUsers() {
        RegisterDto lastNameTooLong = new RegisterDto("register",
                "registerregisterregisterregisterregisterregisterregisterregister", "register",
                "1234", "1234", Gender.MALE, 23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(lastNameTooLong, RegisterValidatorImpl.LAST_NAME_TOO_LONG);
    }

    @Test
    public void userServiceThrowsExceptionWhenUsernameTooLongWhenAddingUsers() {
        RegisterDto usernameTooLong = new RegisterDto("register", "test",
                "registerregisterregisterregisterregisterregisterregisterregisterregister", "1234",
                "1234", Gender.MALE, 23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(usernameTooLong, RegisterValidatorImpl.USERNAME_TOO_LONG);
    }

    @Test
    public void userServiceThrowsExceptionWhenPasswordIsTooLongWhenAddingUsers() {
        RegisterDto passwordTooLong = new RegisterDto("register", "test",
                "user", "1234registerregisterregisterregisterregisterregisterregisterregisterregister",
                "1234registerregisterregisterregisterregisterregisterregisterregisterregister", Gender.MALE,
                23, "Sofia");

        TemplateForUserServiceThrowsExceptionWhenAddingUsers(passwordTooLong, RegisterValidatorImpl.PASSWORD_TOO_LONG);
    }

    private void TemplateForUserServiceThrowsExceptionWhenAddingUsers(RegisterDto registerDto, String exceptionMessage) {
        boolean thrown = false;
        String exception = "";

        Mockito.when(this.userRepository.findByUsername(registerDto.getUsername())).thenReturn(Optional.ofNullable(null));
        Mockito.when(this.userRepository.save(testUser)).thenReturn(testUser);

        try {
            userService.addUser(registerDto);
        } catch (IllegalArgumentException e) {
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals(exceptionMessage, exception);
    }
}
