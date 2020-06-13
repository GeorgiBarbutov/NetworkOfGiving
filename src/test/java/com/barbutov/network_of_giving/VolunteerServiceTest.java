package com.barbutov.network_of_giving;

import com.barbutov.network_of_giving.data.contracts.VolunteerRepository;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.data.models.Volunteer;
import com.barbutov.network_of_giving.services.VolunteerServiceImpl;
import com.barbutov.network_of_giving.services.contracts.VolunteerService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VolunteerServiceTest {
    private User user;
    private Charity charity;
    private VolunteerService volunteerService;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        this.volunteerService = new VolunteerServiceImpl(this.volunteerRepository);
    }

    @Test
    public void AddVolunteerAddsSuccessfully(){
        this.user = new User();
        this.charity = new Charity();

        this.volunteerService.addVolunteer(charity, user);
    }

    @Test
    public void AddVolunteerAddsThrowsExceptionWhenUserIsNull(){
        this.user = null;
        this.charity = new Charity();
        boolean thrown = false;
        String exception = "";

        try{
            this.volunteerService.addVolunteer(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("User can't be null", exception);
    }

    @Test
    public void AddVolunteerAddsThrowsExceptionWhenCharityIsNull(){
        this.user = new User();
        this.charity = null;
        boolean thrown = false;
        String exception = "";

        try{
            this.volunteerService.addVolunteer(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }

    @Test
    public void RemoveVolunteerRemovesSuccessfully(){
        this.user = new User();
        this.charity = new Charity();

        this.volunteerService.removeVolunteer(charity, user);
    }

    @Test
    public void RemoveVolunteerThrowsExceptionWhenUserIsNull(){
        this.user = null;
        this.charity = new Charity();
        boolean thrown = false;
        String exception = "";

        try{
            this.volunteerService.removeVolunteer(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("User can't be null", exception);
    }

    @Test
    public void RemoveVolunteerThrowsExceptionWhenCharityIsNull(){
        this.user = new User();
        this.charity = null;
        boolean thrown = false;
        String exception = "";

        try{
            this.volunteerService.removeVolunteer(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }

    @Test
    public void RemoveAllVolunteersByCharitySuccessfully(){
        this.charity = new Charity();

        this.volunteerService.removeAllVolunteersByCharity(charity);
    }

    @Test
    public void RemoveAllVolunteersByCharityThrowsExceptionWhenCharityIsNull(){
        this.charity = null;
        boolean thrown = false;
        String exception = "";

        try{
            this.volunteerService.removeAllVolunteersByCharity(charity);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }

    @Test
    public void ExistsByCharityAndUserReturnsTrue(){
        this.user = new User();
        this.charity = new Charity();

        Volunteer volunteer = new Volunteer(user, charity);

        Mockito.when(this.volunteerRepository.findByCharityAndUser(charity, user)).thenReturn(Optional.of(volunteer));

        assertTrue(this.volunteerService.existsByCharityAndUser(charity, user));
    }

    @Test
    public void ExistsByCharityAndUserThrowsExceptionWhenUserIsNull(){
        this.user = null;
        this.charity = new Charity();
        boolean thrown = false;
        String exception = "";

        Volunteer volunteer = new Volunteer(user, charity);

        Mockito.when(this.volunteerRepository.findByCharityAndUser(charity, user)).thenReturn(Optional.of(volunteer));

        try{
            this.volunteerService.existsByCharityAndUser(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("User can't be null", exception);
    }

    @Test
    public void ExistsByCharityAndUserThrowsExceptionWhenCharityIsNull(){
        this.user = new User();
        this.charity = null;
        boolean thrown = false;
        String exception = "";

        Volunteer volunteer = new Volunteer(user, charity);

        Mockito.when(this.volunteerRepository.findByCharityAndUser(charity, user)).thenReturn(Optional.of(volunteer));

        try{
            this.volunteerService.existsByCharityAndUser(charity, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }
}
