package com.barbutov.network_of_giving;

import com.barbutov.network_of_giving.data.contracts.DonationRepository;
import com.barbutov.network_of_giving.data.models.Charity;
import com.barbutov.network_of_giving.data.models.User;
import com.barbutov.network_of_giving.services.DonationServiceImpl;
import com.barbutov.network_of_giving.services.contracts.DonationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DonationServiceTest {
    private User user;
    private Charity charity;
    private DonationService donationService;

    @Mock
    private DonationRepository donationRepository;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        this.donationService = new DonationServiceImpl(this.donationRepository);
    }

    @Test
    public void AddDonationAddsSuccessfully(){
        this.user = new User();
        this.charity = new Charity();
        double amount = 20;

        this.donationService.addDonation(charity, amount, user);
    }

    @Test
    public void AddDonationsThrowsExceptionWhenUserIsNull(){
        this.user = null;
        this.charity = new Charity();
        boolean thrown = false;
        String exception = "";
        double amount = 20;

        try{
            this.donationService.addDonation(charity, amount, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("User can't be null", exception);
    }

    @Test
    public void AddDonationThrowsExceptionWhenCharityIsNull(){
        this.user = new User();
        this.charity = null;
        boolean thrown = false;
        String exception = "";
        double amount = 20;

        try{
            this.donationService.addDonation(charity, amount, user);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }

    @Test
    public void RemoveDonationRemovesSuccessfully(){
        this.charity = new Charity();

        this.donationService.removeAllDonationsByCharity(charity);
    }

    @Test
    public void RemoveDonationThrowsExceptionWhenCharityIsNull(){
        this.charity = null;
        boolean thrown = false;
        String exception = "";

        try{
            this.donationService.removeAllDonationsByCharity(charity);

        } catch (IllegalArgumentException e){
            thrown = true;
            exception = e.getMessage();
        }

        assertTrue(thrown);
        assertEquals("Charity can't be null", exception);
    }
}
