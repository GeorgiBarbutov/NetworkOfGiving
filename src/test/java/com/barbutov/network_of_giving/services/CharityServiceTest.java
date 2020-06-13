package com.barbutov.network_of_giving.services;

import com.barbutov.network_of_giving.data.contracts.CharityRepository;
import com.barbutov.network_of_giving.data.dtos.CharityResponseDto;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.contracts.CharityService;
import com.barbutov.network_of_giving.util.CharityEditValidatorImpl;
import com.barbutov.network_of_giving.util.contracts.CharityEditValidator;
import com.barbutov.network_of_giving.util.contracts.CharityValidator;
import com.barbutov.network_of_giving.util.CharityValidatorImpl;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharityServiceTest {
    private Charity charity;
    private CharityService charityService;
    private User creator;

    @Mock
    private CharityRepository charityRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        this.creator = new User();
        this.charity = new Charity("Charity1", "description1", 10,
                100, 0, 0, creator);

        CharityValidator charityValidator = new CharityValidatorImpl(this.charityRepository);
        CharityEditValidator charityEditValidator = new CharityEditValidatorImpl(charityValidator);
        this.charityService = new CharityServiceImpl(this.charityRepository, charityValidator, charityEditValidator);
    }
    @Test
    public void getAllCharitiesAsResponseDtosReturnsAllCharities(){
        List<Charity> charityList = new ArrayList<>();
        charityList.add(new Charity("Charity1", "description1", 10,
                100, 0, 0, creator));
        charityList.add(new Charity("Charity2", "description2", 101,
                1400, 0, 0, creator));
        charityList.add(new Charity("Charity3", "description3", 130,
                2100, 0, 0, creator));

        Mockito.when(this.charityRepository.findAll()).thenReturn(charityList);

        List<CharityResponseDto> returnedCharities = this.charityService.getAllCharitiesAsResponseDtos();
        assertEquals(3, returnedCharities.size());
    }

    @Test
    public void getCharityByIdAsResponseDtoReturnsCharity(){
        long id = 1;

        Mockito.when(this.charityRepository.findById(id)).thenReturn(Optional.of(charity));

        try {
            Charity returnedCharity = this.charityService.getCharityById(id);
            assertEquals(charity.getName(), returnedCharity.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCharityByIdAsResponseDtoThrowsNotFoundException(){
        long id = 1;
        boolean thrown = false;
        String exception = "";

        Mockito.when(this.charityRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        try {
            this.charityService.getCharityById(id);
        } catch (NotFoundException e) {
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity Not Found", exception);
    }

    @Test
    public void getCharityByIdAndCreatorAReturnsCharity(){
        long id = 1;

        Mockito.when(this.charityRepository.findByIdAndCreatorName(id, creator.getUsername()))
                .thenReturn(Optional.of(charity));

        try {
            Charity returnedCharity = this.charityService.getCharityByIdAndCreatorName(id, creator.getUsername());
            assertEquals(charity.getName(), returnedCharity.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCharityByIdAndCreatorThrowsNotFoundException(){
        long id = 1;
        boolean thrown = false;
        String exception = "";

        Mockito.when(this.charityRepository.findByIdAndCreatorName(id, creator.getUsername()))
                .thenReturn(Optional.ofNullable(null));

        try {
            this.charityService.getCharityByIdAndCreatorName(id, creator.getUsername());
        } catch (NotFoundException e) {
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity Not Found", exception);
    }
}
